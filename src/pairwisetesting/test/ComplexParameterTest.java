package pairwisetesting.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import junit.framework.TestCase;
import pairwisetesting.complex.ChildParametersExtractor;
import pairwisetesting.complex.ComplexParameter;
import pairwisetesting.complex.IMethodUnderTestXMLHelper;
import pairwisetesting.complex.IParameterVisitor;
import pairwisetesting.complex.MethodUnderTest;
import pairwisetesting.complex.Parameter;
import pairwisetesting.complex.SimpleParameter;
import pairwisetesting.complex.XStreamMethodUnderTestXMLHelper;
import pairwisetesting.complex.parametervisitor.CountParameterVisitor;
import pairwisetesting.complex.parametervisitor.PrintParameterVisitor;
import pairwisetesting.test.edu.Student;

public class ComplexParameterTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testParameter() {

		Parameter p = new SimpleParameter("java.lang.String", "id");
		assertEquals("java.lang.String", p.getType());
		assertEquals("id", p.getName());
		assertFalse(p.isComplex());
		assertFalse(p.isAbstract());
		
		try {
			p.setDepth(-1);
			fail("The depth should >= 0.");
		} catch (IllegalArgumentException e) {
			
		}

		ComplexParameter st1 = new ComplexParameter("pairwisetesting.test.edu.Student", "student");
		st1.add(p);
		assertEquals("student.id", p.getFullName());
		assertTrue(st1.isComplex());
		assertFalse(p.isAbstract());

		ComplexParameter tc = new ComplexParameter("pairwisetesting.test.edu.Teacher", "teacher");
		Parameter tid = new SimpleParameter("String", "id");
		tc.add(tid);
		st1.add(tc);
		Parameter male = new SimpleParameter("boolean", "male");
		st1.add(male);
		assertEquals("pairwisetesting.test.edu.Student", st1.getType());
		assertEquals("student", st1.getName());
		assertEquals(3, st1.getChildren().length);
		assertEquals(1, tc.getChildren().length);
		assertEquals("student.male", male.getFullName());
		assertEquals("student.teacher.id", tid.getFullName());
		
		// ParameterVisitor
		CountParameterVisitor pv = new CountParameterVisitor();
		st1.accept(pv);
		assertEquals(5, pv.getNodeCount());
		assertEquals(3, pv.getLeafCount());
		IParameterVisitor ppv = new PrintParameterVisitor();
		st1.accept(ppv);
		
		// ComplexParameter & isAbstract
		ComplexParameter interface1 = new ComplexParameter("java.util.List", "children");
		assertTrue(interface1.isAbstract());	
		ComplexParameter abstractClass1 = new ComplexParameter("java.util.AbstractList", "children");
		assertTrue(abstractClass1.isAbstract());
		ComplexParameter concreteClass1 = new ComplexParameter("java.util.ArrayList", "children");
		assertFalse(concreteClass1.isAbstract());
	}

	public void testMethodUnderTest() {
		SimpleParameter p1 = new SimpleParameter("int", "number");
		ComplexParameter p2 = new ComplexParameter("Student", "student");
		p2.add(new SimpleParameter("String", "id"));
		ComplexParameter p3 = new ComplexParameter("Teacher", "teacher");
		p3.add(new SimpleParameter("String", "id"));

		MethodUnderTest m = new MethodUnderTest("void", "foo");
		m.add(p1);
		m.add(p2);
		m.add(p3);

		assertEquals("void", m.getReturnType());
		assertEquals("foo", m.getName());
		assertEquals(3, m.getParameters().length);
	}

	@SuppressWarnings("unchecked")
	public void testMethodUnderTestXMLHelper() {
		SimpleParameter p1 = new SimpleParameter("int", "number");
		ComplexParameter p2 = new ComplexParameter(
				"pairwisetesting.test.edu.Student", "student");
		SimpleParameter sid = new SimpleParameter("String", "id");
		p2.add(sid);
		ComplexParameter p3 = new ComplexParameter(
				"pairwisetesting.test.edu.Teacher", "teacher");
		SimpleParameter tid = new SimpleParameter("String", "id");
		p3.add(tid);
		p2.add(p3);

		MethodUnderTest m = new MethodUnderTest("int", "foo");
		m.add(p1);
		m.add(p2);
		IMethodUnderTestXMLHelper helper = new XStreamMethodUnderTestXMLHelper();
		String xml = helper.toXML(m);
		// System.out.println(xml);
		assertEquals(m, helper.fromXML(xml));
		
		// Simple Return Value
		Object[] objects = helper.assign(xml, new String[] { "100", "s001", "t001", "100"});
		assertEquals(100, Integer.parseInt(objects[0].toString()));
		assertEquals("s001", ((Student)objects[1]).getId());
		assertEquals("t001", ((Student)objects[1]).getTeacher().getId());
		assertEquals(100, Integer.parseInt(objects[2].toString()));
		
		// Complex Return Value
		ComplexParameter returnValue
				= new ComplexParameter("pairwisetesting.test.edu.Student", "ReturnValue");
		returnValue.add(new SimpleParameter("java.lang.String", "id"));
		ComplexParameter tc2 = new ComplexParameter("pairwisetesting.test.edu.Teacher", "teacher");
		tc2.add(new SimpleParameter("java.lang.String", "id"));
		returnValue.add(tc2);
		m.setReturnValueRarameter(returnValue);
		xml = helper.toXML(m);
		// System.out.println(xml);
		objects = helper.assign(xml, new String[] { "100", "s001", "t001", "A001", "T001"});
		assertEquals(100, Integer.parseInt(objects[0].toString()));
		assertEquals("s001", ((Student)objects[1]).getId());
		assertEquals("t001", ((Student)objects[1]).getTeacher().getId());
		assertEquals("A001", ((Student)objects[2]).getId());
		assertEquals("T001", ((Student)objects[2]).getTeacher().getId());
		
		// Abstract Type
		ComplexParameter eduManager1
			= new ComplexParameter("pairwisetesting.test.edu.IEducationManager", "eduManager");
		assertTrue(eduManager1.isAbstract());
		ComplexParameter student1 = new ComplexParameter(
				"pairwisetesting.test.edu.Student", "student");
		SimpleParameter studentId = new SimpleParameter("String", "id");
		student1.add(studentId);
		ComplexParameter teacher1 = new ComplexParameter(
				"pairwisetesting.test.edu.Teacher", "teacher");
		SimpleParameter teacherId = new SimpleParameter("String", "id");
		teacher1.add(teacherId);
		student1.add(teacher1);
		
		m = new MethodUnderTest("double", "doStatistics");
		m.add(eduManager1);
		m.add(student1);
		xml = helper.toXML(m);
		// System.out.println(xml);
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.EducationManager1", 
				"S002", "T002", 
				"96.0"});
		assertEquals("pairwisetesting.test.edu.EducationManager1",
				((pairwisetesting.test.edu.EducationManager1)objects[0]).getClass().getName());
		assertEquals("S002", ((Student)objects[1]).getId());
		assertEquals("T002", ((Student)objects[1]).getTeacher().getId());
		assertEquals(96.0, Double.parseDouble(objects[2].toString()));
		
		ComplexParameter eduManager2
			= new ComplexParameter("pairwisetesting.test.edu.AbstractEducationManager", "eduManager");
		assertTrue(eduManager2.isAbstract());
		eduManager2.add(new SimpleParameter("double", "sum"));
		eduManager2.add(new SimpleParameter("double", "average"));
		ComplexParameter student2 = new ComplexParameter(
				"pairwisetesting.test.edu.Student", "student");
		SimpleParameter studentId2 = new SimpleParameter("java.lang.String", "id");
		student2.add(studentId2);
		ComplexParameter teacher2 = new ComplexParameter(
				"pairwisetesting.test.edu.Teacher", "teacher");
		SimpleParameter teacherId2 = new SimpleParameter("java.lang.String", "id");
		teacher2.add(teacherId2);
		student2.add(teacher2);
		m = new MethodUnderTest("double", "doStatistics");
		m.add(eduManager2);
		m.add(student2);
		xml = helper.toXML(m);
		// System.out.println(xml);
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.EducationManager2", "350.0", "89.0",
				"S003", "T003", 
				"85.0"});
		assertEquals("pairwisetesting.test.edu.EducationManager2",
				((pairwisetesting.test.edu.EducationManager2)objects[0]).getClass().getName());
		assertEquals(350.0,
				((pairwisetesting.test.edu.EducationManager2)objects[0]).getSum());
		assertEquals(89.0,
				((pairwisetesting.test.edu.EducationManager2)objects[0]).getAverage());
		assertEquals("S003", ((Student)objects[1]).getId());
		assertEquals("T003", ((Student)objects[1]).getTeacher().getId());
		assertEquals(85.0, Double.parseDouble(objects[2].toString()));
		
		// Simple Array Type
		ComplexParameter sorter
			= new ComplexParameter("pairwisetesting.test.edu.ISorter", "sorter");
		assertTrue(sorter.isAbstract());
		SimpleParameter scores = new SimpleParameter("double[]", "scores");
		m = new MethodUnderTest("double[]", "sort");
		m.add(sorter);
		m.add(scores);
		xml = helper.toXML(m);
		// System.out.println(xml);
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"[ 67.0 , 89.0 ,  87.0, 90.0 ]",
				" [67.0 , 87.0, 89.0, 90.0] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("double[]",
				((double[])objects[1]).getClass().getCanonicalName());
		double[] arr2 = (double[])objects[1];
		assertTrue(Arrays.equals(new double[] {67.0, 89.0, 87.0, 90.0}, arr2));
		double[] result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {67.0, 87.0, 89.0, 90.0}, result));
		
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.BubbleSorter", 
				"[90.0]",
				"[90.0]"});
		assertEquals("pairwisetesting.test.edu.BubbleSorter",
				((pairwisetesting.test.edu.BubbleSorter)objects[0]).getClass().getName());
		arr2 = (double[])objects[1];
		assertTrue(Arrays.equals(new double[] {90.0}, arr2));
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {90.0}, result));
		
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.BubbleSorter", 
				"[ ]",
				"[]"});
		arr2 = (double[])objects[1];
		assertTrue(Arrays.equals(new double[0], arr2));
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[0], result));
		
		// Simple Container Type - HashSet
		sorter
			= new ComplexParameter("pairwisetesting.test.edu.ISorter", "sorter");
		assertTrue(sorter.isAbstract());
		scores = new SimpleParameter("java.util.HashSet<Double>", "scores");
		m = new MethodUnderTest("double[]", "sort1");
		m.add(sorter);
		m.add(scores);
		xml = helper.toXML(m);
		// System.out.println(xml);
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"{ 67.0 , 89.0 ,  87.0, 90.0 }",
				" [67.0 , 87.0, 89.0, 90.0] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("java.util.HashSet",
				((HashSet)objects[1]).getClass().getCanonicalName());
		HashSet<Double> expectedSet = new HashSet<Double>();
		expectedSet.add(67.0);
		expectedSet.add(89.0);
		expectedSet.add(87.0);
		expectedSet.add(90.0);
		HashSet<Double> set	= (HashSet<Double>)objects[1];
		// System.out.println(set);
		assertEquals(expectedSet, set);
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {67.0, 87.0, 89.0, 90.0}, result));
		
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"{ 67.0  }",
				" [67.0 ] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("java.util.HashSet",
				((HashSet)objects[1]).getClass().getCanonicalName());
		expectedSet = new HashSet<Double>();
		expectedSet.add(67.0);
		set	= (HashSet<Double>)objects[1];
		// System.out.println(set);
		assertEquals(expectedSet, set);
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {67.0}, result));
		
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {67.0}, result));
		
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"{ }",
				" [] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("java.util.HashSet",
				((HashSet)objects[1]).getClass().getCanonicalName());
		expectedSet = new HashSet<Double>();
		set	= (HashSet<Double>)objects[1];
		// System.out.println(set);
		assertEquals(expectedSet, set);
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {}, result));
		
		// Simple Container Type - ArrayList
		sorter
			= new ComplexParameter("pairwisetesting.test.edu.ISorter", "sorter");
		assertTrue(sorter.isAbstract());
		scores = new SimpleParameter("java.util.ArrayList<Double>", "scores");
		m = new MethodUnderTest("double[]", "sort2");
		m.add(sorter);
		m.add(scores);
		xml = helper.toXML(m);
		// System.out.println(xml);
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"( 67.0 , 89.0 ,  87.0, 90.0 )",
				" [67.0 , 87.0, 89.0, 90.0] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("java.util.ArrayList",
				((ArrayList)objects[1]).getClass().getCanonicalName());
		ArrayList<Double> expectedList = new ArrayList<Double>();
		expectedList.add(67.0);
		expectedList.add(89.0);
		expectedList.add(87.0);
		expectedList.add(90.0);
		ArrayList<Double> list = (ArrayList<Double>)objects[1];
		// System.out.println(list);
		assertEquals(expectedList, list);
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {67.0, 87.0, 89.0, 90.0}, result));
		
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"( 67.0  )",
				" [67.0 ] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("java.util.ArrayList",
				((ArrayList)objects[1]).getClass().getCanonicalName());
		expectedList = new ArrayList<Double>();
		expectedList.add(67.0);
		list = (ArrayList<Double>)objects[1];
		// System.out.println(list);
		assertEquals(expectedList, list);
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {67.0}, result));
		
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {67.0}, result));
		
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"( )",
				" [] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("java.util.ArrayList",
				((ArrayList)objects[1]).getClass().getCanonicalName());
		expectedList = new ArrayList<Double>();
		list = (ArrayList<Double>)objects[1];
		// System.out.println(list);
		assertEquals(expectedList, list);
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {}, result));
		
		// Simple Container Type - LinkedList
		sorter
			= new ComplexParameter("pairwisetesting.test.edu.ISorter", "sorter");
		assertTrue(sorter.isAbstract());
		scores = new SimpleParameter("java.util.LinkedList<Double>", "scores");
		m = new MethodUnderTest("double[]", "sort3");
		m.add(sorter);
		m.add(scores);
		xml = helper.toXML(m);
		// System.out.println(xml);
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"( 67.0 , 89.0 ,  87.0, 90.0 )",
				" [67.0 , 87.0, 89.0, 90.0] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("java.util.LinkedList",
				((LinkedList)objects[1]).getClass().getCanonicalName());
		LinkedList<Double> expectedList2 = new LinkedList<Double>();
		expectedList2.add(67.0);
		expectedList2.add(89.0);
		expectedList2.add(87.0);
		expectedList2.add(90.0);
		LinkedList<Double> list2 = (LinkedList<Double>)objects[1];
		// System.out.println(list);
		assertEquals(expectedList2, list2);
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {67.0, 87.0, 89.0, 90.0}, result));
		
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"( 67.0  )",
				" [67.0 ] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("java.util.LinkedList",
				((LinkedList)objects[1]).getClass().getCanonicalName());
		expectedList2 = new LinkedList<Double>();
		expectedList2.add(67.0);
		list2 = (LinkedList<Double>)objects[1];
		// System.out.println(list);
		assertEquals(expectedList2, list2);
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {67.0}, result));
		
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {67.0}, result));
		
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"( )",
				" [] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("java.util.LinkedList",
				((LinkedList)objects[1]).getClass().getCanonicalName());
		expectedList2 = new LinkedList<Double>();
		list2 = (LinkedList<Double>)objects[1];
		// System.out.println(list);
		assertEquals(expectedList2, list2);
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {}, result));
		
		// Simple Container Type - HashMap
		sorter = new ComplexParameter("pairwisetesting.test.edu.ISorter", "sorter");
		assertTrue(sorter.isAbstract());
		scores = new SimpleParameter("java.util.HashMap<String, Double>", "scores");
		m = new MethodUnderTest("double[]", "sort4");
		m.add(sorter);
		m.add(scores);
		xml = helper.toXML(m);
		// System.out.println(xml);
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"{ a: 67.0 , b : 89.0 ,  c:87.0, d:90.0 }",
				" [67.0 , 87.0, 89.0, 90.0] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("java.util.HashMap",
				((HashMap)objects[1]).getClass().getCanonicalName());
		HashMap<String, Double> expectedmap2 = new HashMap<String, Double>();
		expectedmap2.put("a", 67.0);
		expectedmap2.put("b", 89.0);
		expectedmap2.put("c", 87.0);
		expectedmap2.put("d", 90.0);
		HashMap<String, Double> map2 = (HashMap<String, Double>)objects[1];
		// System.out.println(map);
		assertEquals(expectedmap2, map2);
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {67.0, 87.0, 89.0, 90.0}, result));
		
		sorter = new ComplexParameter("pairwisetesting.test.edu.ISorter", "sorter");
		assertTrue(sorter.isAbstract());
		scores = new SimpleParameter("java.util.HashMap<String, Double>", "scores");
		m = new MethodUnderTest("double[]", "sort4");
		m.add(sorter);
		m.add(scores);
		xml = helper.toXML(m);
		// System.out.println(xml);
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"{ b : 89.0 }",
				" [ 89.0] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("java.util.HashMap",
				((HashMap)objects[1]).getClass().getCanonicalName());
		expectedmap2 = new HashMap<String, Double>();
		expectedmap2.put("b", 89.0);
		map2 = (HashMap<String, Double>)objects[1];
		// System.out.println(map);
		assertEquals(expectedmap2, map2);
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {89.0}, result));
		
		sorter = new ComplexParameter("pairwisetesting.test.edu.ISorter", "sorter");
		assertTrue(sorter.isAbstract());
		scores = new SimpleParameter("java.util.HashMap<String, Double>", "scores");
		m = new MethodUnderTest("double[]", "sort4");
		m.add(sorter);
		m.add(scores);
		xml = helper.toXML(m);
		// System.out.println(xml);
		objects = helper.assign(xml, new String[] { 
				"pairwisetesting.test.edu.QuickSorter", 
				"{ }",
				" [] "});
		assertEquals("pairwisetesting.test.edu.QuickSorter",
				((pairwisetesting.test.edu.QuickSorter)objects[0]).getClass().getName());
		assertEquals("java.util.HashMap",
				((HashMap)objects[1]).getClass().getCanonicalName());
		expectedmap2 = new HashMap<String, Double>();
		map2 = (HashMap<String, Double>)objects[1];
		// System.out.println(map);
		assertEquals(expectedmap2, map2);
		result = (double[])objects[2];
		assertTrue(Arrays.equals(new double[] {}, result));
	}
	
	public void testChildParametersExtractor(){
		String className = "pairwisetesting.test.edu.Student";
		ChildParametersExtractor e = new ChildParametersExtractor();
		Parameter[] ps = e.getParameters(className);
		
		Parameter[] expected = new Parameter[2];
		SimpleParameter sid = new SimpleParameter("java.lang.String", "id");
		expected[0] = sid;
		ComplexParameter teacher = new ComplexParameter(
				"pairwisetesting.test.edu.Teacher", "teacher");
		SimpleParameter tid = new SimpleParameter("java.lang.String", "id");
		teacher.add(tid);
		expected[1] = teacher;

		assertTrue("It should be equal", Arrays.equals(expected, ps));
	}
}
