package org.nzhegalin.estimate;

import java.util.Map;
import java.util.TreeMap;

import org.nzhegalin.estimate.dao.DAOFactory;
import org.nzhegalin.estimate.entity.MachineResource;
import org.nzhegalin.estimate.entity.ManhourResource;
import org.nzhegalin.estimate.entity.MaterialResource;
import org.nzhegalin.estimate.entity.Resource;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class ResourceWindow extends Window {

	private static final long serialVersionUID = 1L;

	protected static final String MACHINE_RESOURCE = "Машины";
	protected static final String MANHOUR_RESOURCE = "Человеческие";
	protected static final String MATERIAL_RESOURCE = "Материалы";

	private Resource resourceValue;
	protected DAOFactory daoFactory;

	private final Select materialTypeSelect = new Select("Тип ресурса");
	private final Form form = new Form(null, new FormFieldFactory() {

		private static final long serialVersionUID = 1L;

		@Override
		public Field createField(Item item, Object propertyId, Component uiContext) {
			Map<String, String> captions = new TreeMap<String, String>();
			captions.put("code", "Код");
			captions.put("measureUnit", "Единица измерения");
			captions.put("name", "Наименование");
			String pid = (String) propertyId;
			if (captions.containsKey(pid))
				return new TextField(captions.get(pid));

			return null;

		}
	});

	public ResourceWindow() {
		super();
		form.setVisible(false);
		materialTypeSelect.addItem(MACHINE_RESOURCE);
		materialTypeSelect.addItem(MANHOUR_RESOURCE);
		materialTypeSelect.addItem(MATERIAL_RESOURCE);
		materialTypeSelect.setImmediate(true);
		materialTypeSelect.setNullSelectionAllowed(false);
		materialTypeSelect.setNewItemsAllowed(false);
		materialTypeSelect.addListener(new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				String materialType = (String) event.getProperty().getValue();
				if (MACHINE_RESOURCE.equals(materialType)) {
					initializeForm(new MachineResource());
				} else if (MANHOUR_RESOURCE.equals(materialType)) {
					initializeForm(new ManhourResource());
				} else if (MATERIAL_RESOURCE.equals(materialType)) {
					initializeForm(new MaterialResource());
				}
			}
		});
		setSizeFull();
		Button createButton = new Button("Создать");

		createButton.addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				form.commit();
				@SuppressWarnings("unchecked")
				BeanItem<Resource> item = (BeanItem<Resource>) form.getItemDataSource();
				Resource value = item.getBean();
				daoFactory.getResourceProvider().createNewResource(value);
				close();
			}
		});

		Button updateButton = new Button("Обновить");
		/*
		 * updateButton.addListener(new ClickListener() { private static final
		 * long serialVersionUID = 1L;
		 * 
		 * @Override public void buttonClick(ClickEvent event) { form.commit();
		 * 
		 * @SuppressWarnings("unchecked") BeanItem<DictionaryValue> item =
		 * (BeanItem<DictionaryValue>) form .getItemDataSource();
		 * DictionaryValue value = item.getBean(); new
		 * DictionaryValueProvider().updateNewDictionaryValue(value); close(); }
		 * });
		 */
		boolean isNew = resourceValue == null || resourceValue.getId() == 0;
		createButton.setVisible(isNew);
		updateButton.setVisible(!isNew);

		addComponent(materialTypeSelect);
		addComponent(form);
		addComponent(createButton);
		addComponent(updateButton);

	}

	public ResourceWindow(Resource resourceValue) {
		this();
		this.setResourceValue(resourceValue);

		initializeForm(resourceValue);

	}

	@SuppressWarnings("unchecked")
	private void initializeForm(Resource resource) {
		form.setVisible(true);
		BeanItem<Resource> item = new BeanItem<Resource>(resource);
		if (form.getItemDataSource() != null) {
			item = (BeanItem<Resource>) form.getItemDataSource();
			Resource oldResource = item.getBean();
			resource.setCode(oldResource.getCode());
			resource.setMeasureUnit(oldResource.getMeasureUnit());
			resource.setName(oldResource.getName());
			item = new BeanItem<Resource>(resource);
		}
		form.setItemDataSource(item);
		form.setSizeFull();
	}

	private void setResourceValue(Resource resourceValue) {
		this.resourceValue = resourceValue;
	}

	public Resource getResourceValue() {
		return resourceValue;
	}

	public DAOFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

}
