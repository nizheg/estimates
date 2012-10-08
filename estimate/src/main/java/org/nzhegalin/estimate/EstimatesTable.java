package org.nzhegalin.estimate;

import org.nzhegalin.estimate.entity.DictionaryValue;
import org.nzhegalin.estimate.entity.DictionaryValueResource;
import org.nzhegalin.estimate.entity.Estimates;
import org.nzhegalin.estimate.entity.Estimates.EstimatesItem;
import org.nzhegalin.estimate.entity.Resource;
import org.nzhegalin.estimate.manager.EstimatesProvider;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class EstimatesTable extends Table {

	private static final long serialVersionUID = 1L;

	private static final String NUMBER_COL_ID = "number";
	private static final String CODE_COL_ID = "code";
	private static final String NAME_COL_ID = "name";
	private static final String MEASURE_UNIT_COL_ID = "measureUnit";
	private static final String AMOUNT_COL_ID = "amount";
	private static final String COMMON_AMOUNT_COL_ID = "commonAmount";

	private static final String CHAPTER_ID_PREFIX = "chapter_";

	private Estimates estimates;

	private int chapterCounter = 1;

	public EstimatesTable() {
		super();
		setSortDisabled(true);
		initializeColumns();
		setSizeFull();
		addActionHandler(new Action.Handler() {

			private static final long serialVersionUID = 1L;

			private final Action EDIT_ACTION = new Action("Изменить");
			private final Action DELETE_ACTION = new Action("Удалить");

			private final Action[] possibleActions = new Action[] {
					EDIT_ACTION, DELETE_ACTION };

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (EDIT_ACTION.equals(action)) {
					editItem((Long) target);
				} else if (DELETE_ACTION.equals(action)) {
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

		addListener(new ItemClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick() && event.getItemId() instanceof Long) {
					editItem((Long) event.getItemId());
				}
			}
		});
	}

	@Override
	protected String formatPropertyValue(Object rowId, Object colId,
			Property property) {
		if (property.getValue() == null) {
			return "";
		}
		if (property.getType() == Double.class) {
			return String.format("%f", (Double) property.getValue());
		}
		return property.getValue().toString();
	};

	private class EditItemWindow extends Window {

		private static final long serialVersionUID = 1L;
		protected final EstimatesItem estimatesItem;
		private final TextField editor = new TextField("Количество общее:");
		private final Button okButton = new Button("OK");
		private final Button cancelButton = new Button("Cancel");

		public EditItemWindow(EstimatesItem item) {
			super("Задать новое значение");
			this.estimatesItem = item;
			setModal(true);

			GridLayout layout = new GridLayout(2, 2);
			setContent(layout);
			layout.setMargin(true);
			layout.setSpacing(true);

			editor.setValue(item.getMeasure());
			layout.addComponent(editor, 0, 0, 1, 0);
			okButton.addListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					estimatesItem.setMeasure(Double.valueOf(editor.getValue()
							.toString()));
					new EstimatesProvider().updateEstimateItem(estimatesItem);
					fillByEstimates(estimates);
					closeWindow();
				}
			});

			layout.addComponent(okButton);
			cancelButton.addListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					closeWindow();
				}
			});
			layout.addComponent(cancelButton);

		}

		protected void closeWindow() {
			getParent().removeWindow(this);
		}
	}

	protected void editItem(Long itemId) {
		final Window subwindow = new EditItemWindow(
				estimates.getItemById(itemId));
		getWindow().addWindow(subwindow);
	}

	protected void deleteItem(Long itemId) {
		EstimatesItem item = estimates.getItemById(itemId);
		new EstimatesProvider().deleteEstimateItem(item);
		estimates.deleteItem(item);
		fillByEstimates(estimates);
	}

	private void initializeColumns() {
		addContainerProperty(NUMBER_COL_ID, Integer.class, null);
		setColumnHeader(NUMBER_COL_ID, "№ п/п");
		addContainerProperty(CODE_COL_ID, String.class, null);
		setColumnHeader(CODE_COL_ID, "Шифр, номера нормативов и коды ресурсов");
		addContainerProperty(NAME_COL_ID, String.class, null);
		setColumnHeader(NAME_COL_ID,
				"Наименование работ и затрат, характеристика оборудования и его масса");
		addContainerProperty(MEASURE_UNIT_COL_ID, String.class, null);
		setColumnHeader(MEASURE_UNIT_COL_ID, "Единица измерения");
		addContainerProperty(AMOUNT_COL_ID, Double.class, null);
		setColumnHeader(AMOUNT_COL_ID, "Количество на единицу");
		addContainerProperty(COMMON_AMOUNT_COL_ID, Double.class, null);
		setColumnHeader(COMMON_AMOUNT_COL_ID, "Количество общее");
	}

	public void clear() {
		setCaption("");
		super.removeAllItems();
		this.setEstimates(null);
	}

	public void fillByEstimates(Estimates estimates) {
		clear();
		this.setEstimates(estimates);
		setCaption(estimates.getName());
		int number = 1;
		for (EstimatesItem estimatesItem : estimates.getValues()) {
			addEstimatesItem(number, estimatesItem);
			number++;
		}
	}

	public void addChapter(String text) {
		Item chapterItem = addItem(CHAPTER_ID_PREFIX + chapterCounter);
		chapterCounter++;
		chapterItem.getItemProperty(NAME_COL_ID).setValue(text);
	}

	public void addEstimatesItem(int number, EstimatesItem estimatesItem) {
		Item tableItem = addItem(estimatesItem.getId());
		Double commonAmount = estimatesItem.getMeasure();
		tableItem.getItemProperty(NUMBER_COL_ID).setValue(number);

		DictionaryValue itemDictionaryValue = estimatesItem.getValue();
		tableItem.getItemProperty(CODE_COL_ID).setValue(
				itemDictionaryValue.getDictionary().getCode()
						+ itemDictionaryValue.getCode());
		tableItem.getItemProperty(NAME_COL_ID).setValue(
				itemDictionaryValue.getName());
		tableItem.getItemProperty(MEASURE_UNIT_COL_ID).setValue(
				itemDictionaryValue.getMeasureUnit());
		tableItem.getItemProperty(COMMON_AMOUNT_COL_ID).setValue(commonAmount);

		for (DictionaryValueResource dictionaryValueResource : itemDictionaryValue
				.getResources()) {
			addDictionaryValueResourceItem(estimatesItem.getId(),
					dictionaryValueResource, commonAmount);
		}

	}

	private void addDictionaryValueResourceItem(long estimatesItemId,
			DictionaryValueResource dictionaryValueResource, Double commonAmount) {
		Resource resource = dictionaryValueResource.getResource();
		Item tableItem = addItem(getResourceRowId(estimatesItemId,
				resource.getId()));
		tableItem.getItemProperty(CODE_COL_ID).setValue(resource.getCode());
		tableItem.getItemProperty(NAME_COL_ID).setValue(resource.getName());
		tableItem.getItemProperty(MEASURE_UNIT_COL_ID).setValue(
				resource.getMeasureUnit());
		final Double amount = dictionaryValueResource.getMeasure();
		tableItem.getItemProperty(AMOUNT_COL_ID).setValue(
				dictionaryValueResource.getMeasure());
		tableItem.getItemProperty(COMMON_AMOUNT_COL_ID).setValue(
				amount * commonAmount);
	}

	private String getResourceRowId(long estimatesItemId, long resourceId) {
		return estimatesItemId + "_" + resourceId;
	}

	public void setEstimates(Estimates estimates) {
		this.estimates = estimates;
	}

	public Estimates getEstimates() {
		return estimates;
	}

}
