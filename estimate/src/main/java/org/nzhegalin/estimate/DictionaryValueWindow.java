package org.nzhegalin.estimate;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.nzhegalin.estimate.dao.DAOFactory;
import org.nzhegalin.estimate.entity.Dictionary;
import org.nzhegalin.estimate.entity.DictionaryValue;
import org.nzhegalin.estimate.entity.DictionaryValueResource;
import org.nzhegalin.estimate.entity.Resource;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.event.Action;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class DictionaryValueWindow extends Window {

	private static final long serialVersionUID = 1L;

	protected DAOFactory daoFactory;

	private final DictionaryValue dictionaryValue;
	protected boolean isCommit = false;

	private static class DictionaryValueResources extends Table {
		public static final String RESOURCE = "resource";
		public static final String MEASURE = "measure";

		private static final long serialVersionUID = 1L;
		private DictionaryValue dictionaryValue;

		public DictionaryValueResources(String name) {
			super(name);

			addContainerProperty(RESOURCE, Resource.class, null);
			setColumnHeader(RESOURCE, "Ресурс");
			addContainerProperty(MEASURE, Double.class, null);
			setColumnHeader(MEASURE, "Измерение");
			setTableFieldFactory(new TableFieldFactory() {
				private static final long serialVersionUID = 1L;

				@Override
				public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
					Field field = new TextField();
					if (RESOURCE.equals(propertyId)) {
						field.setReadOnly(true);
					} else if (MEASURE.equals(propertyId)) {
						field.addValidator(new DoubleValidator("Измерение должно быть числом"));
						field.setRequired(true);
					}
					return field;
				}

			});
			addActionHandler(new Action.Handler() {

				private static final long serialVersionUID = 1L;

				private final Action DELETE_ACTION = new Action("Удалить");

				private final Action[] possibleActions = new Action[] { DELETE_ACTION };

				@Override
				public void handleAction(Action action, Object sender, Object target) {
					if (DELETE_ACTION.equals(action)) {
						deleteItem((Long) target);
					}
				}

				@Override
				public Action[] getActions(Object target, Object sender) {
					if (target instanceof Long)
						return possibleActions;
					return new Action[0];
				}
			});
			setEditable(true);
		}

		protected void deleteItem(Long itemId) {
			removeItem(itemId);
		}

		@Override
		public void commit() throws SourceException, InvalidValueException {
			super.commit();
			Set<DictionaryValueResource> dictionaryValueResources = dictionaryValue.getResources();
			dictionaryValueResources.clear();
			@SuppressWarnings("unchecked")
			Collection<Long> ids = (Collection<Long>) getItemIds();
			for (Long id : ids) {
				Item tableItem = getItem(id);
				DictionaryValueResource res = new DictionaryValueResource();
				res.setResource((Resource) tableItem.getItemProperty(RESOURCE).getValue());
				res.setMeasure((Double) tableItem.getItemProperty(MEASURE).getValue());
				dictionaryValueResources.add(res);
			}
		}

		public void setDictionaryValue(DictionaryValue dictionaryValue) {
			this.dictionaryValue = dictionaryValue;
			for (DictionaryValueResource dictionaryValueResource : dictionaryValue.getResources()) {
				addResource(dictionaryValueResource.getResource(), dictionaryValueResource.getMeasure());
			}
		}

		public void addResource(Resource resource, Double measure) {
			if (resource == null)
				return;
			Item item = addItem(resource.getId());
			item.getItemProperty(RESOURCE).setValue(resource);
			item.getItemProperty(MEASURE).setValue(measure);
		}
	}

	private final DictionaryValueResources table = new DictionaryValueResources("Ресурсы");
	private final Form form = new Form(null, new FormFieldFactory() {

		private static final long serialVersionUID = 1L;

		@Override
		public Field createField(Item item, Object propertyId, Component uiContext) {
			Map<String, String> captions = new TreeMap<String, String>();
			captions.put("code", "Код");
			captions.put("measureUnit", "Единица измерения");
			captions.put("name", "Наименование");
			String pid = (String) propertyId;
			if ("dictionary".equals(pid)) {
				Select select = new Select("Справочник");
				Collection<Dictionary> dictionaries = daoFactory.getDictionaryDAO().getAllDictionaries();
				for (Dictionary dictionary : dictionaries) {
					select.addItem(dictionary);
				}
				select.setNewItemsAllowed(false);
				return select;
			} else if ("resources".equals(pid)) {
				return table;
			} else if (captions.containsKey(pid))
				return new TextField(captions.get(pid));

			return null;

		}
	});

	public DictionaryValueWindow(DAOFactory daoFactory, DictionaryValue dictionaryValue) {
		super();
		this.daoFactory = daoFactory;
		setSizeFull();

		this.dictionaryValue = dictionaryValue;
		BeanItem<DictionaryValue> item = new BeanItem<DictionaryValue>(dictionaryValue);
		table.setDictionaryValue(dictionaryValue);
		form.setItemDataSource(item);
		form.setSizeFull();
		form.getField("dictionary").setRequired(true);

		Select resourceSelect = new Select("Доступные ресурсы");
		resourceSelect.setNewItemsAllowed(false);
		resourceSelect.setNullSelectionAllowed(false);
		Collection<Resource> resources = daoFactory.getResourceDAO().getAllResources();
		for (Resource resource : resources) {
			resourceSelect.addItem(resource);
		}
		resourceSelect.setNewItemsAllowed(false);
		resourceSelect.setImmediate(true);
		resourceSelect.addListener(new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				table.addResource((Resource) event.getProperty().getValue(), .0);
			}
		});

		Button createButton = new Button("Создать");
		createButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				form.commit();
				table.commit();
				@SuppressWarnings("unchecked")
				BeanItem<DictionaryValue> item = (BeanItem<DictionaryValue>) form.getItemDataSource();
				DictionaryValue value = item.getBean();
				DictionaryValueWindow.this.daoFactory.getDictionaryValueDAO().createNewDictionaryValue(value);
				close();
			}
		});
		Button updateButton = new Button("Обновить");
		updateButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				form.commit();
				table.commit();
				@SuppressWarnings("unchecked")
				BeanItem<DictionaryValue> item = (BeanItem<DictionaryValue>) form.getItemDataSource();
				DictionaryValue value = item.getBean();
				DictionaryValueWindow.this.daoFactory.getDictionaryValueDAO().updateDictionaryValue(value);
				close();
			}
		});
		boolean isNew = dictionaryValue == null || dictionaryValue.getId() == 0;
		createButton.setVisible(isNew);
		updateButton.setVisible(!isNew);

		addComponent(form);
		addComponent(resourceSelect);
		addComponent(createButton);
		addComponent(updateButton);
	}

	public DictionaryValue getDictionaryValue() {
		return dictionaryValue;
	}

	public boolean isCommit() {
		return isCommit;
	}

	public DAOFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
}
