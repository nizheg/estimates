package org.nzhegalin.estimate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nzhegalin.estimate.dao.DAOFactory;
import org.nzhegalin.estimate.entity.Dictionary;
import org.nzhegalin.estimate.entity.DictionaryValue;
import org.nzhegalin.estimate.entity.Estimates;
import org.nzhegalin.estimate.entity.Estimates.EstimatesItem;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.Action;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class Main extends Application {

	private static final long serialVersionUID = 1L;
	private Window mainWindow;
	private final EstimatesTable estimatesTable = new EstimatesTable();
	private final Button addButton = new Button("Добавить в смету");
	private Long estimatesId;
	private final Tree dictionariesTree = new Tree();
	private final ComboBox estimatesSelection = new ComboBox("Выберите сохраненную смету");
	protected DAOFactory daoFactory;

	public DAOFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void init() {
		estimatesTable.setDaoFactory(daoFactory);
		mainWindow = new Window("Smeta");
		mainWindow.setSizeFull();
		setMainWindow(mainWindow);

		estimatesSelection.setImmediate(true);
		estimatesSelection.setNewItemsAllowed(true);
		estimatesSelection.setNewItemHandler(new NewItemHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public void addNewItem(String newItemCaption) {
				if (newItemCaption != null && !newItemCaption.isEmpty()) {
					getDaoFactory().getEstimatesProvider().createNewEstimates(newItemCaption);
				}
				updateEstimatesSelection();
				estimatesSelection.setValue(null);
			}
		});
		estimatesSelection.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Estimates selectedEstimatesValue = (Estimates) event.getProperty().getValue();
				if (selectedEstimatesValue != null) {
					estimatesId = selectedEstimatesValue.getId();
					estimatesTable.fillByEstimates(getDaoFactory().getEstimatesProvider().getEstimates(estimatesId));
				} else {
					estimatesId = null;
					estimatesTable.clear();
				}
				updateAddEstimatesItemButtonState();

			}
		});
		addComponent(estimatesSelection);
		updateEstimatesSelection();

		estimatesTable.setImmediate(true);
		addComponent(estimatesTable);

		updateDictionariesTree();
		dictionariesTree.setImmediate(true);
		dictionariesTree.addActionHandler(new Action.Handler() {

			private static final long serialVersionUID = 1L;

			private final Action EDIT_ACTION = new Action("Редактировать");

			private final Action[] possibleActions = new Action[] { EDIT_ACTION };

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (EDIT_ACTION.equals(action)) {
					openDictionaryWindow(getDaoFactory().getDictionaryValueProvider().getFullDictionaryValue(
							((DictionaryValue) target).getId()));
				}
			}

			@Override
			public Action[] getActions(Object target, Object sender) {
				if (target instanceof DictionaryValue)
					return possibleActions;
				return new Action[0];
			}
		});
		dictionariesTree.addListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateAddEstimatesItemButtonState();
			}
		});

		addButton.addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Object value = dictionariesTree.getValue();
				if (value instanceof DictionaryValue) {
					EstimatesItem estimatesItem = new EstimatesItem();
					estimatesItem.setValue((DictionaryValue) value);
					getDaoFactory().getEstimatesProvider()
							.addEstimateItem(estimatesTable.getEstimates(), estimatesItem);
					reloadTable();
				}
			}

		});

		addComponent(dictionariesTree);
		addComponent(addButton);
		updateAddEstimatesItemButtonState();

		Button createDictionaryValueButton = new Button("Создать новую запись в справочнике");
		createDictionaryValueButton.addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				openDictionaryWindow(new DictionaryValue());
			}
		});
		addComponent(createDictionaryValueButton);

		Button createResourceButton = new Button("Создать новый ресурс в справочнике");
		createResourceButton.addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ResourceWindow dictionaryValueWindow = new ResourceWindow();
				dictionaryValueWindow.setDaoFactory(daoFactory);
				dictionaryValueWindow.setModal(true);
				mainWindow.addWindow(dictionaryValueWindow);
			}
		});
		addComponent(createResourceButton);

		Button createAct = new Button("Сформировать отчет");
		createAct.addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ReportWindow actWindow = new ReportWindow(estimatesTable.getEstimates());
				actWindow.setSizeFull();
				mainWindow.addWindow(actWindow);
			}
		});
		addComponent(createAct);

	}

	private void updateDictionariesTree() {
		dictionariesTree.removeAllItems();
		List<Dictionary> dictionaries = new ArrayList<Dictionary>(getDaoFactory().getDictionaryProvider()
				.getAllDictionaries());
		for (Dictionary dictionary : dictionaries) {
			dictionariesTree.addItem(dictionary);
			dictionariesTree.setChildrenAllowed(dictionary, false);
		}

		Collection<DictionaryValue> dictionaryValues = getDaoFactory().getDictionaryValueProvider()
				.getAllDictionaryValues();

		for (DictionaryValue dictionaryValue : dictionaryValues) {
			dictionariesTree.addItem(dictionaryValue);
			dictionariesTree.setChildrenAllowed(dictionaryValue, false);
			int index = dictionaries.indexOf(dictionaryValue.getDictionary());
			Dictionary dictionary;
			if (index == -1) {
				dictionary = dictionaryValue.getDictionary();
				dictionaries.add(dictionary);
			} else {
				dictionary = dictionaries.get(index);
			}
			dictionariesTree.setChildrenAllowed(dictionary, true);
			dictionariesTree.setParent(dictionaryValue, dictionary);
		}
	}

	private void updateEstimatesSelection() {
		Collection<Estimates> allEstimates = getDaoFactory().getEstimatesProvider().getAllEstimates();
		estimatesSelection.removeAllItems();
		for (Estimates estimates : allEstimates) {
			estimatesSelection.addItem(estimates);
		}
	}

	private void updateAddEstimatesItemButtonState() {
		if (estimatesId == null || !(dictionariesTree.getValue() instanceof DictionaryValue)) {
			addButton.setEnabled(false);
		} else {
			addButton.setEnabled(true);
		}
	}

	private void addComponent(Component component) {
		mainWindow.addComponent(component);
	}

	protected void reloadTable() {
		if (estimatesId != null) {
			this.estimatesTable.fillByEstimates(getDaoFactory().getEstimatesProvider().getEstimates(estimatesId));
		}
	}

	private void openDictionaryWindow(DictionaryValue dictionaryValue) {
		DictionaryValueWindow dictionaryValueWindow = new DictionaryValueWindow(daoFactory, dictionaryValue);

		dictionaryValueWindow.setModal(true);
		dictionaryValueWindow.addListener(new CloseListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				updateDictionariesTree();
			}

		});
		mainWindow.addWindow(dictionaryValueWindow);
	}

}
