package org.nzhegalin.estimate;

import java.io.InputStream;
import java.util.List;

import org.nzhegalin.estimate.entity.Estimates;
import org.nzhegalin.estimate.entity.Report;
import org.nzhegalin.estimate.entity.ReportRow;
import org.nzhegalin.estimate.entity.Resource;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

public class ReportWindow extends Window {

	private static final long serialVersionUID = 1L;

	private final Report report;
	private final Button generateExcelFileButton = new Button(
			"Сформировать Excel файл");

	private final Table table = new Table() {

		private static final long serialVersionUID = 1L;

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

	};
	private static final String NAME_KEY = "name";
	private static final String MEASURE_KEY = "measure";
	private static final String CODE_KEY = "code";
	private static final String ACTUAL_MEASURE_KEY = "actualMeasure";

	private static final String RES_COMMON_PREFIX = "common_";
	private static final String RES_MADE_PREFIX = "made_";

	public ReportWindow(Estimates estimates) {
		super();
		addComponent(table);
		table.setSortDisabled(true);
		table.setSizeFull();
		this.report = new Report(estimates);
		printReport();

		addComponent(generateExcelFileButton);
		generateExcelFileButton.addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ReportWindow.this.open(new StreamResource(new StreamSource() {

					private static final long serialVersionUID = 1L;

					@Override
					public InputStream getStream() {
						return new ExcelReportGenerator(report).generate();
					}
				}, "report.xls", getApplication()));
			}
		});

	}

	public void printReport() {
		table.removeAllItems();

		table.addContainerProperty(NAME_KEY, String.class, null);
		table.setColumnHeader(NAME_KEY, "Название");
		table.addContainerProperty(MEASURE_KEY, String.class, null);
		table.setColumnHeader(MEASURE_KEY, "Единица измерения");
		table.addContainerProperty(CODE_KEY, String.class, null);
		table.setColumnHeader(CODE_KEY, "Код");
		table.addContainerProperty(ACTUAL_MEASURE_KEY, Double.class, null);
		table.setColumnHeader(ACTUAL_MEASURE_KEY,
				"Количество фактически выполненных работ");

		List<Resource> resources = report.getResources();
		for (Resource resource : resources) {
			String idOfCommon = RES_COMMON_PREFIX + resource.getId();
			table.addContainerProperty(idOfCommon, Double.class, null);
			table.setColumnHeader(idOfCommon, resource.getName()
					+ ". Норма на единицу работ");
			table.setColumnWidth(idOfCommon, 30);
			String idOfMade = RES_MADE_PREFIX + resource.getId();
			table.addContainerProperty(idOfMade, Double.class, null);
			table.setColumnHeader(idOfMade, resource.getName()
					+ ". На выполненный объем работ");
			table.setColumnWidth(idOfMade, 30);
		}

		for (ReportRow row : report.getRows()) {
			Item tableItem = table.addItem(row.getId());

			Property nameProp = tableItem.getItemProperty(NAME_KEY);
			nameProp.setValue(row.getName());

			Property measureProp = tableItem.getItemProperty(MEASURE_KEY);
			measureProp.setValue(row.getMeasureUnit());

			Property codeProp = tableItem.getItemProperty(CODE_KEY);
			codeProp.setValue(row.getCode());

			Property actualMeasureProp = tableItem
					.getItemProperty(ACTUAL_MEASURE_KEY);
			actualMeasureProp.setValue(row.getActualMeasure());

			for (Resource res : report.getResources()) {
				Property common = tableItem.getItemProperty(RES_COMMON_PREFIX
						+ res.getId());
				common.setValue(row.getResourceMeasure(res));
				Property made = tableItem.getItemProperty(RES_MADE_PREFIX
						+ res.getId());
				made.setValue(row.getReourceActualMeasure(res));
			}

		}

	}
}
