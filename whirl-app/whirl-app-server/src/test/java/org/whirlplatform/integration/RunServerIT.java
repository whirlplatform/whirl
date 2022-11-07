package org.whirlplatform.integration;

//import DBInitUtil;
//import DataType;
//import EventsTest2Page;
//import EventsTestPage;
//import FilterType;
//import GridDataExtractor;
//import GridEditingTestPage;
//import GridEditingTestUtil;
//import GridTestPage;
//import GridTestPage.SortPanelButtons;
//import GridTestPage.SortPanelSide;
//import GridTestRowModel;
//import JSEventsTestPage;
//import LoginPage;
//import LoginSuccessPage;
//import SortType;
//import TestTableSet;
//import TransmissionParameters;
//import WebArchiveUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.whirlplatform.integration.db.DBInitUtil;
import org.whirlplatform.integration.db.GridDataExtractor;
import org.whirlplatform.integration.db.TestTableSet;
import org.whirlplatform.integration.event.EventsTest2Page;
import org.whirlplatform.integration.event.EventsTestPage;
import org.whirlplatform.integration.event.TransmissionParameters;
import org.whirlplatform.integration.grid.GridEditingTestPage;
import org.whirlplatform.integration.grid.GridEditingTestUtil;
import org.whirlplatform.integration.grid.GridTestPage;
import org.whirlplatform.integration.grid.GridTestRowModel;
import org.whirlplatform.integration.login.LoginPage;
import org.whirlplatform.integration.login.LoginSuccessPage;
import org.whirlplatform.integration.page.event.JSEventsTestPage;
import org.whirlplatform.integration.util.WebArchiveUtil;
import org.whirlplatform.meta.shared.FilterType;
import org.whirlplatform.meta.shared.SortType;
import org.whirlplatform.meta.shared.data.DataType;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class RunServerIT {

	public static final String WHIRL_PROJECT_VERSION = "3.9.0-SNAPSHOT";

	@ArquillianResource
	URL contextPath;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		System.out.println("Starting config");
		WebArchive testWar = WebArchiveUtil.createTestWar(WHIRL_PROJECT_VERSION);
		return testWar;
	}

	@BeforeClass
	public static void preapreDB() {
		System.out.println("Starting prepare DB for testing!");
		DBInitUtil.prepareDB();
	}

	@AfterClass
	public static void cleanDB () {
		System.out.println("Clean DB after testing!");
		DBInitUtil.cleanDB(TestTableSet.get());
	}

	@Test
	@InSequence(0)
	@RunAsClient
	public void loginTest(@InitialPage LoginPage loginPage) {
		loginPage.waitForPageLoading();
		loginPage.login("arquillian", "12345");
		LoginSuccessPage successPage = Graphene.goTo(LoginSuccessPage.class);
		Assert.assertTrue(successPage.isSuccess());
	}

	// @Ignore
	@Test
	@InSequence(1)
	@RunAsClient
	public void testGrid(@InitialPage GridTestPage gridTestPage) {
		// TODO: Необходимо реализовать сравнение с приведением к единому
		// формату
		// для дат и чисел с плавающей запятой.
		gridTestPage.waitForPageLoading();
		GridDataExtractor extractor = new GridDataExtractor(DBInitUtil.CONFIG);
		extractor.extractDbData("SELECT * FROM unicore.grid_test where rownum <= 50 order by id");
		gridTestPage.configure(extractor.getColumnNames());
		gridTestPage.setSelectorValue(50);
		// Читаем данные все данные из БД,сравниваем с данными грида.
		Map<Integer, GridTestRowModel> fromDbData = extractor.getRowModels();
		Map<Integer, GridTestRowModel> fromGrid = gridTestPage.parseGrid();
		assertEquals(fromDbData.size(), fromGrid.size());
		for (Integer key : fromDbData.keySet()) {
			GridTestRowModel modelFromDb = fromDbData.get(key);
			GridTestRowModel modelFromGrid = fromGrid.get(key);
			assertEquals(modelFromDb, modelFromGrid);
		}
		// Проверяем, что количество рядов равно выбранному в селекторе
		// пагинатора.
		for (int rowCount = 40; rowCount >= 20; rowCount -= 10) {
			gridTestPage.setSelectorValue(rowCount);
			int gridRowCount = gridTestPage.parseGrid().size();
			assertEquals(rowCount, gridRowCount);
		}
		gridTestPage.configure(Arrays.asList("ID"));
		gridTestPage.setSelectorValue(10);
		assertEquals(gridTestPage.parseGrid().size(), 10);

		gridTestPage.changeGridPage(1);
		checkRange(1, 10, gridTestPage.parseGrid());

		gridTestPage.goToNextPage();
		checkRange(11, 20, gridTestPage.parseGrid());

		gridTestPage.goToLastButton();
		checkRange(41, 50, gridTestPage.parseGrid());

		gridTestPage.goToPrevPage();
		checkRange(31, 40, gridTestPage.parseGrid());

		gridTestPage.goToFirstPage();
		checkRange(1, 10, gridTestPage.parseGrid());

		gridTestPage.setPageNum(3);
		checkRange(21, 30, gridTestPage.parseGrid());

		gridTestPage.setFilter("ID", FilterType.EQUALS.toString(), "1");
		assertEquals(1, gridTestPage.parseGrid().size());
		checkBaseFilters(gridTestPage.parseGrid(), FilterType.EQUALS, "ID", String.valueOf(1));

		gridTestPage.clearFilter();
		assertEquals(10, gridTestPage.parseGrid().size());
		// Что-бы было видно все содержимое грида
		gridTestPage.setSelectorValue(500);

		gridTestPage.configure(Arrays.asList("STRINGS"));
		gridTestPage.setFilter("STRINGS", FilterType.EMPTY.toString(), null);
		checkBaseFilters(gridTestPage.parseGrid(), FilterType.EMPTY, "STRINGS", null);

		gridTestPage.setFilter("STRINGS", FilterType.NOT_EMPTY.toString(), null);
		checkBaseFilters(gridTestPage.parseGrid(), FilterType.NOT_EMPTY, "STRINGS", null);

		gridTestPage.setFilter("STRINGS", FilterType.CONTAINS.toString(), "2");
		checkStringsFilters(gridTestPage.parseGrid(), FilterType.CONTAINS, "STRINGS", "2");

		gridTestPage.setFilter("STRINGS", FilterType.NOT_CONTAINS.toString(), "1");
		checkStringsFilters(gridTestPage.parseGrid(), FilterType.NOT_CONTAINS, "STRINGS", "1");

		gridTestPage.setFilter("STRINGS", FilterType.START_WITH.toString(), "string1");
		checkStringsFilters(gridTestPage.parseGrid(), FilterType.START_WITH, "STRINGS", "string1");

		gridTestPage.setFilter("STRINGS", FilterType.END_WITH.toString(), "3");
		checkStringsFilters(gridTestPage.parseGrid(), FilterType.END_WITH, "STRINGS", "3");

		gridTestPage.clearFilter();
		gridTestPage.configure(Arrays.asList("NUMBERS"));
		gridTestPage.setFilter("NUMBERS", FilterType.EQUALS.toString(), "32");
		checkBaseFilters(gridTestPage.parseGrid(), FilterType.EQUALS, "NUMBERS", "32");

		gridTestPage.setFilter("NUMBERS", FilterType.GREATER.toString(), "1000");
		checkNumberFilters(gridTestPage.parseGrid(), FilterType.GREATER, "NUMBERS", "1000");

		gridTestPage.setFilter("NUMBERS", FilterType.LOWER.toString(), "1000");
		checkNumberFilters(gridTestPage.parseGrid(), FilterType.LOWER, "NUMBERS", "1000");

		gridTestPage.setFilter("NUMBERS", FilterType.BETWEEN.toString(), "10", "300");
		checkBetweenCondition(gridTestPage.parseGrid(), "NUMBERS", "10", "300");

		gridTestPage.clearFilter();
		gridTestPage.openSortPanel();
		assertEquals(5, gridTestPage.getFromPanelElementsCount(GridTestPage.SortPanelSide.LEFT));
		gridTestPage.clickBarButton(GridTestPage.SortPanelButtons.ALL_RIGHT);
		assertEquals(0, gridTestPage.getFromPanelElementsCount(GridTestPage.SortPanelSide.LEFT));
		assertEquals(5, gridTestPage.getFromPanelElementsCount(GridTestPage.SortPanelSide.RIGHT));

		gridTestPage.clickBarButton(GridTestPage.SortPanelButtons.ALL_LEFT);
		assertEquals(0, gridTestPage.getFromPanelElementsCount(GridTestPage.SortPanelSide.RIGHT));
		assertEquals(5, gridTestPage.getFromPanelElementsCount(GridTestPage.SortPanelSide.LEFT));

		gridTestPage.selectFromPanelItem("ID");
		gridTestPage.clickBarButton(GridTestPage.SortPanelButtons.RIGHT);
		gridTestPage.changeSort("ID");
		gridTestPage.configure(Arrays.asList("ID"));
		checkColDirection(gridTestPage.parseGrid(), "ID", SortType.DESC, true);

		gridTestPage.clickBarButton(GridTestPage.SortPanelButtons.ALL_LEFT);
		gridTestPage.selectFromPanelItem("STRINGS");
		gridTestPage.clickBarButton(GridTestPage.SortPanelButtons.RIGHT);
		gridTestPage.changeSort("STRINGS");
		gridTestPage.configure(Arrays.asList("STRINGS"));
		checkColDirection(gridTestPage.parseGrid(), "STRINGS", SortType.DESC, false);
		gridTestPage.changeSort("STRINGS");
		checkColDirection(gridTestPage.parseGrid(), "STRINGS", SortType.ASC, false);
		gridTestPage.closeSortPanel();
	}

	private void checkRange(int bottomBound, int topBound, Map<Integer, GridTestRowModel> rows) {
		Set<Integer> ids = rows.keySet();
		int max = Collections.max(ids);
		int min = Collections.min(ids);
		assertEquals(bottomBound, min);
		assertEquals(topBound, max);
	}

	/**
	 * Проверяет, что все ячейки определенной колонки отображаемые в гриде
	 * соответствуют определенному условию.
	 *
	 * @param rows
	 * @param type
	 * @param colName
	 * @param condition
	 */
	private void checkNumberFilters(Map<Integer, GridTestRowModel> rows, FilterType type, String colName,
			String condition) {
		for (GridTestRowModel row : rows.values()) {
			switch (type) {
			case GREATER:
				assertTrue(Double.valueOf(row.getColumnValue(colName)) > Double.valueOf(condition));
				break;
			case LOWER:
				assertTrue(Double.valueOf(row.getColumnValue(colName)) < Double.valueOf(condition));
				break;
			default:
				// Валим тест если не попали в тип фильтра.
				assertTrue(false);
				break;
			}
		}
	}

	private void checkBaseFilters(Map<Integer, GridTestRowModel> rows, FilterType type, String colName,
			String condition) {
		for (GridTestRowModel row : rows.values()) {
			switch (type) {
			case EQUALS:
				assertEquals(condition, row.getColumnValue(colName));
				break;
			case EMPTY:
				assertTrue(row.getColumnValue(colName).isEmpty());
				break;
			case NOT_EMPTY:
				assertTrue(!row.getColumnValue(colName).isEmpty());
				break;
			default:
				// Валим тест если не попали в тип фильтра.
				assertTrue(false);
				break;
			}
		}
	}

	private void checkStringsFilters(Map<Integer, GridTestRowModel> rows, FilterType type, String colName,
			String condition) {
		for (GridTestRowModel row : rows.values()) {
			switch (type) {
			case CONTAINS:
				assertTrue(row.getColumnValue(colName).contains(condition));
				break;
			case NOT_CONTAINS:
				assertTrue(!row.getColumnValue(colName).contains(condition));
				break;
			case START_WITH:
				assertTrue(row.getColumnValue(colName).startsWith(condition));
				break;
			case END_WITH:
				assertTrue(row.getColumnValue(colName).endsWith(condition));
				break;
			default:
				// Валим тест если не попали в тип фильтра.
				assertTrue(false);
				break;
			}
		}
	}

	private void checkBetweenCondition(Map<Integer, GridTestRowModel> rows, String colName, String val1, String val2) {
		checkNumberFilters(rows, FilterType.GREATER, colName, val1);
		checkNumberFilters(rows, FilterType.LOWER, colName, val2);
	}

	private void checkColDirection(LinkedHashMap<Integer, GridTestRowModel> rows, String colName, SortType sortType,
			boolean compareAsNumbers) {
		String prevVal = null;
		for (Map.Entry<Integer, GridTestRowModel> entry : rows.entrySet()) {
			GridTestRowModel row = entry.getValue();
			String currentVal = row.getColumnValue(colName);
			if (prevVal == null) {
				prevVal = currentVal;
			} else {
				if (prevVal.isEmpty() && !currentVal.isEmpty()) {
					prevVal = currentVal;
					continue;
				} else if (!prevVal.isEmpty() && currentVal.isEmpty()) {
					continue;
				}
				compareValues(prevVal, currentVal, sortType, compareAsNumbers);
				prevVal = currentVal;
			}
		}
	}

	private void compareValues(String prevVal, String currentVal, SortType sortType, boolean compareAsNumbers) {
		if (compareAsNumbers) {
			if (SortType.ASC.equals(sortType)) {
				assertTrue(Double.valueOf(prevVal) <= Double.valueOf(currentVal));
			} else {
				assertTrue(Double.valueOf(prevVal) >= Double.valueOf(currentVal));
			}
		} else {
			if (SortType.ASC.equals(sortType)) {
				assertTrue(prevVal.compareTo(currentVal) <= 0);
			} else {
				assertTrue(prevVal.compareTo(currentVal) >= 0);
			}
		}
	}

	// @Ignore
	@Test
	@InSequence(2)
	@RunAsClient
	public void testEvents(@InitialPage EventsTestPage eventsTestPage) throws InterruptedException {
		eventsTestPage.waitForPageLoading();

		TransmissionParameters outcome1 = new TransmissionParameters("textik1", "24", "11.11.2000 10:11:10", true);
		TransmissionParameters income1 = eventsTestPage.transmitOracleToOracle(outcome1);
		assertEquals(outcome1, income1);

		TransmissionParameters outcome2 = new TransmissionParameters("textik2", "25", "12.11.2000 10:12:10", true);
		TransmissionParameters income2 = eventsTestPage.transmitOracleToJs(outcome2);
		assertEquals(outcome2, income2);

		TransmissionParameters outcome3 = new TransmissionParameters("textik3", "26", "13.11.2000 10:13:10", true);
		TransmissionParameters income3 = eventsTestPage.transmitJsToOracle(outcome3);
		assertEquals(outcome3, income3);

		TransmissionParameters outcome4 = new TransmissionParameters("textik4", "27", "14.11.2000 10:14:10", true);
		TransmissionParameters income4 = eventsTestPage.transmitJsToJs(outcome4);
		assertEquals(outcome4, income4);
	}

	// @Ignore
	@Test
	@InSequence(3)
	@RunAsClient
	public void testEvents2(@InitialPage EventsTest2Page eventsTest2Page) throws InterruptedException {
		eventsTest2Page.waitForPageLoading();
		final String EXPECTED_ID_1 = "1000000";
		final String EXPECTED_ID_2 = "1000006";
		final String RESULT_CLEARED = "empty";
		final String SELECTION_CLEARED = "empty";
		final String EXPECTED_1 = "1000000";
		final String EXPECTED_2 = "1000000,1000001,1000004";
		/*
		 * Тест передачи как параметра первого значения ComboBox
		 */
		assertEquals(EXPECTED_ID_1, eventsTest2Page.makeComboBoxSelection1());
		assertEquals(EXPECTED_ID_1, eventsTest2Page.clickComboBoxJs());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickComboBoxClear());
		assertEquals(EXPECTED_ID_1, eventsTest2Page.clickComboBoxOracle());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickComboBoxClear());
		/*
		 * Тест передачи как параметра второго значения в ComboBox
		 */
		assertEquals(EXPECTED_ID_2, eventsTest2Page.makeComboBoxSelection2());
		assertEquals(EXPECTED_ID_2, eventsTest2Page.clickComboBoxJs());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickComboBoxClear());
		assertEquals(EXPECTED_ID_2, eventsTest2Page.clickComboBoxOracle());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickComboBoxClear());
		/*
		 * Тест очистки ComboBox
		 */
		assertEquals(SELECTION_CLEARED, eventsTest2Page.clearComboBoxSelection());
		/*
		 * Тест передачи как параметра одной строки MultiComboBox
		 */
		assertEquals(EXPECTED_1, eventsTest2Page.makeMultiComboBoxSelection1());
		assertEquals(EXPECTED_1, eventsTest2Page.clickMultiComboBoxJs());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickMultiComboBoxClear());
		assertEquals(EXPECTED_1, eventsTest2Page.clickMultiComboBoxOracle());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickMultiComboBoxClear());
		/*
		 * Тест передачи как параметра трех строк MultiComboBox
		 */
		assertEquals(EXPECTED_2, eventsTest2Page.makeMultiComboBoxSelection2());
		assertEquals(EXPECTED_2, eventsTest2Page.clickMultiComboBoxJs());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickMultiComboBoxClear());
		assertEquals(EXPECTED_2, eventsTest2Page.clickMultiComboBoxOracle());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickMultiComboBoxClear());
		/*
		 * Тест очистки MultiComboBox
		 */
		assertEquals(SELECTION_CLEARED, eventsTest2Page.clearMultiComboBoxSelections());
		/*
		 * Тест передачи как параметра одной строки Tree
		 */
		eventsTest2Page.expandTree();
		assertEquals(EXPECTED_1, eventsTest2Page.makeTreeSelection1());
		assertEquals(EXPECTED_1, eventsTest2Page.clickTreeJs());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickTreeClear());
		assertEquals(EXPECTED_1, eventsTest2Page.clickTreeOracle());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickTreeClear());
		/*
		 * Тест передачи как параметра трех строк Tree
		 */
		assertEquals(EXPECTED_2, eventsTest2Page.makeTreeSelection2());
		assertEquals(EXPECTED_2, eventsTest2Page.clickTreeJs());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickTreeClear());
		assertEquals(EXPECTED_2, eventsTest2Page.clickTreeOracle());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickTreeClear());
		/*
		 * Тест очистки выбранных позиций в Tree
		 */
		assertEquals(SELECTION_CLEARED, eventsTest2Page.clearTreeSelections());
		/*
		 * Тест передачи как параметра одной строки Grid
		 */
		assertEquals(EXPECTED_1, eventsTest2Page.makeGridSelection1());
		assertEquals(EXPECTED_1, eventsTest2Page.clickGridJs());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickGridClear());
		assertEquals(EXPECTED_1, eventsTest2Page.clickGridOracle());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickGridClear());
		/*
		 * Тест передачи как параметра трех строк Grid
		 */
		assertEquals(EXPECTED_2, eventsTest2Page.makeGridSelection2());
		assertEquals(EXPECTED_2, eventsTest2Page.clickGridJs());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickGridClear());
		assertEquals(EXPECTED_2, eventsTest2Page.clickGridOracle());
		assertEquals(RESULT_CLEARED, eventsTest2Page.clickGridClear());
		/*
		 * Тест очистки выбора в Grid
		 */
		assertEquals(SELECTION_CLEARED, eventsTest2Page.clearGridSelections());
	}

	/**
	 * Проверка того, что описано в документации к классу
	 * Events
	 *
	 * @param jsEventsTestPage
	 * @throws InterruptedException
	 */
	// @Ignore
	@Test
	@InSequence(4)
	@RunAsClient
	public void testJSEvents(@InitialPage JSEventsTestPage jsEventsTestPage) throws InterruptedException {
		/*
		 * Сначала выполняю все события, устанавливающие значения, затем жду
		 * пока все компоненты-участники теста готовы, и только потом проверяю
		 * полученные значения. Это позволяет почти мгновенно проходить тест.
		 * Если же для каждого компонента сначала устанавливать значение, потом
		 * ждать готовности, потом проверять, - то тест длится секунд 10-15.
		 * Недопустимо.
		 */
		jsEventsTestPage.waitForPageLoading();

		// проверка передачи текстового значения
		// выполняю глобальное событие evt_click_global приложения
		// arquillian_event_test
		String expectedTextFieldValue = "test param value";
		jsEventsTestPage.executeEvent("evt_click_global", expectedTextFieldValue, DataType.STRING.toString());
		// компоненты и события приложения должны записать это значение в
		// текстовую область. Результат необходимо проверить.
		// jsEventsTestPage.

		// проверка передачи числового значения
		Double expectedNumberFieldValue = 559.12;
		jsEventsTestPage.executeEvent("evt_click_global", expectedNumberFieldValue, DataType.NUMBER.toString());
		String sGottenNumberFieldValue = jsEventsTestPage.getNumberFieldValue();
		if (sGottenNumberFieldValue != null) {
			// лучше определить текущую локаль и использовать
			// NumberFormat.getInstance(Locale.<CURRENT_LOCALE>).parseDouble...
			sGottenNumberFieldValue = sGottenNumberFieldValue.replace(",", ".");
		}

		// проверка передачи даты
		String strDate = "21/12/2012 12:34:56"; // тестируемое значение
		Date expectedDateFieldValue = null;
		try { // передаю значение приложению
			SimpleDateFormat baseSdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			expectedDateFieldValue = baseSdf.parse(strDate);
			Calendar c = Calendar.getInstance();
			c.setTime(expectedDateFieldValue);
			String jsStrDate = String.format("%s-%s-%sT%s:%s:%s", c.get(Calendar.YEAR),
					c.get(Calendar.MONTH) + 1 /* т.к. отсчёт происходит с 0 */, c.get(Calendar.DAY_OF_MONTH),
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));// "2011-10-10T14:48:00";
			jsEventsTestPage.executeEvent("evt_click_global", jsStrDate, DataType.DATE.toString());

		} catch (ParseException e) {
			e.printStackTrace();
			assertEquals("Исключение " + e, false, true);
		}

		// проверка передачи логического значения
		boolean expectedCheckBoxValue = true;
		jsEventsTestPage.executeEvent("evt_click_global", expectedCheckBoxValue, DataType.BOOLEAN.toString());

		// жду готовности сразу всех элементов
		jsEventsTestPage.waitWhenComponentsReady();

		// теперь проверяю корректность полученных данных сразу для всех простых
		// компонентов
		// (кроме multicombobox, т.к. с ним посложнее. Он проверяется последним
		// отдельно)
		String gottenTextFieldValue = jsEventsTestPage.getTextFieldValue();
		assertEquals(expectedTextFieldValue, gottenTextFieldValue);

		Double gottenNumberFieldValue = Double.parseDouble(sGottenNumberFieldValue);
		assertEquals(expectedNumberFieldValue, gottenNumberFieldValue);

		// ожидается формат, установленный в редакторе форм: yyyy.MM.dd HH:mm:ss
		SimpleDateFormat localizedSdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		String sGotten = jsEventsTestPage.getDateFieldValue();
		Date gottenDateFieldValue;
		try {
			gottenDateFieldValue = localizedSdf.parse(sGotten);
			assertEquals(expectedDateFieldValue, gottenDateFieldValue);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean gottenCheckBoxValue = jsEventsTestPage.getBooleanFieldValue();
		assertEquals(expectedCheckBoxValue, gottenCheckBoxValue);

		// отдельно проверяю передачу спискового значения. Т.к. техника
		// сохранения\получения данных отличается от техники работы с простыми
		// компонентами (textfield, numberfield, checkbox..)
		{
			String[] ar = { "some value 1", "another value2", "and other value 3" };
			List<String> listExpected = Arrays.asList(ar);
			jsEventsTestPage.executeEvent("evt_click_global", ar, DataType.LIST.toString());
			List<String> listGotten = jsEventsTestPage.getMultiComboBoxValues();
			assertEquals(true, listGotten.containsAll(listExpected) && listExpected.containsAll(listGotten));
		}
	}

	/**
	 * Интеграционный тест редактирования, удаления, добавления и копирования
	 * строк в EditGridBuilder как непосредственно в компоненте, так и через
	 * форму редактирования записей.
	 */
	// @Ignore
	@Test
	@InSequence(5)
	@RunAsClient
	public void testGridEditing(@InitialPage GridEditingTestPage gridEditingTestPage) throws InterruptedException {
		gridEditingTestPage.waitForPageLoading();
		gridEditingTestPage.init();
		/*
		 * Чистим таблицу в базе от записей которые будем добавлять
		 */
		GridEditingTestUtil.deleteTestDatabaseRows();
		final Map<Integer, GridTestRowModel> dbTestRows = GridEditingTestUtil.readTestDatabaseRows();
		assertEquals(0, dbTestRows.size());
		/*
		 * Тест вставки первой тестовой записи в грид через форму
		 */
		final GridTestRowModel FIRST = GridEditingTestUtil.createFirstRow();
		gridEditingTestPage.insertRowUsingForm(FIRST);
		assertEquals(FIRST, gridEditingTestPage.readGridRow(FIRST.getId()));
		assertEquals(FIRST, GridEditingTestUtil.readDatabaseRow(FIRST.getId()));
		/*
		 * Тест вставки второй тестовой записи в грид через форму
		 */
		final GridTestRowModel SECOND = GridEditingTestUtil.createSecondRow();
		gridEditingTestPage.insertRowUsingForm(SECOND);
		assertEquals(SECOND, gridEditingTestPage.readGridRow(SECOND.getId()));
		assertEquals(SECOND, GridEditingTestUtil.readDatabaseRow(SECOND.getId()));
		/*
		 * Тест обновления тестовых записей в гриде через форму
		 */
		final GridTestRowModel FIRST_UPDATED = GridEditingTestUtil.createFirstUpdatedRow();
		final GridTestRowModel SECOND_UPDATED = GridEditingTestUtil.createSecondUpdatedRow();
		gridEditingTestPage.updateRowsUsingForm(FIRST_UPDATED, SECOND_UPDATED);
		assertEquals(FIRST_UPDATED, gridEditingTestPage.readGridRow(FIRST_UPDATED.getId()));
		assertEquals(FIRST_UPDATED, GridEditingTestUtil.readDatabaseRow(FIRST_UPDATED.getId()));
		assertEquals(SECOND_UPDATED, gridEditingTestPage.readGridRow(SECOND_UPDATED.getId()));
		assertEquals(SECOND_UPDATED, GridEditingTestUtil.readDatabaseRow(SECOND_UPDATED.getId()));
		/*
		 * Тест копирования тестовых записей в гриде через форму
		 */
		final GridTestRowModel FIRST_COPIED = GridEditingTestUtil.createFirstRowCopy();
		final GridTestRowModel SECOND_COPIED = GridEditingTestUtil.createSecondRowCopy();
		gridEditingTestPage.copyRowsUsingForm(FIRST_COPIED, SECOND_COPIED);
		assertEquals(FIRST_COPIED, gridEditingTestPage.readGridRow(FIRST_COPIED.getId()));
		assertEquals(FIRST_COPIED, GridEditingTestUtil.readDatabaseRow(FIRST_COPIED.getId()));
		assertEquals(SECOND_COPIED, gridEditingTestPage.readGridRow(SECOND_COPIED.getId()));
		assertEquals(SECOND_COPIED, GridEditingTestUtil.readDatabaseRow(SECOND_COPIED.getId()));
		/*
		 * Тест inline редактирования
		 */
		GridTestRowModel FIRST_TRICKED = GridEditingTestUtil.createFirstRow();
		FIRST_TRICKED.addValue("DFSTRING",
				gridEditingTestPage.readGridRow(FIRST_TRICKED.getId()).getColumnValue("DFSTRING"));
		gridEditingTestPage.updateRowInline(FIRST_TRICKED);
		assertEquals(FIRST_TRICKED, gridEditingTestPage.readGridRow(FIRST_TRICKED.getId()));
		assertEquals(FIRST_TRICKED, GridEditingTestUtil.readDatabaseRow(FIRST_TRICKED.getId()));

		/*
		 * Тест удаления тестовых записей
		 */
		gridEditingTestPage.deleteGridRows(FIRST.getId(), SECOND.getId(), FIRST_COPIED.getId(), SECOND_COPIED.getId());
		Assert.assertNull(gridEditingTestPage.readGridRow(FIRST.getId()));
		Assert.assertNull(gridEditingTestPage.readGridRow(SECOND.getId()));
		Assert.assertNull(gridEditingTestPage.readGridRow(FIRST_COPIED.getId()));
		Assert.assertNull(gridEditingTestPage.readGridRow(SECOND_COPIED.getId()));
		Assert.assertNull(GridEditingTestUtil.readDatabaseRow(FIRST.getId()));
		Assert.assertNull(GridEditingTestUtil.readDatabaseRow(SECOND.getId()));
		Assert.assertNull(GridEditingTestUtil.readDatabaseRow(FIRST_COPIED.getId()));
		Assert.assertNull(GridEditingTestUtil.readDatabaseRow(SECOND_COPIED.getId()));
	}
}
