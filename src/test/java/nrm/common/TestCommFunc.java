import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ${10190990} on 2017/8/10.
 */
public class TestCommFunc {
	@Test
	public void testCutOffLDN(){
		String func = "${Version+='_Patch'}";
		func = func.substring(2, func.lastIndexOf('}'));
		String oneArr = "abc.cde";
		int dot = oneArr.indexOf('.');
		String mm = oneArr.substring(0, dot);
		String ff = oneArr.substring(dot+1);
		Method method = null;
		String a = "";
		try{
			method = CommFunc.class.getMethod("cutOffLDN", new Class[] {String.class, String.class});
			 a = (String)method.invoke(null, "A=a,B=b,C=c", "B");
		}catch(Exception e){
			e.printStackTrace();
		}
		assertEquals("A=a,B=b",a);
	}


	@Test
	public void testJoin3List(){
		List<String> a = new ArrayList<String>();
		a.add("zdh");
		a.add("zz");
		a.add("bb");
		List<String> b = new ArrayList<String>();
		b.add("zdh");
		b.add("zz");
		b.add("bb");
		List<String> c = new ArrayList<String>();
		c.add("zdh");
		c.add("zz");
		c.add("bb");
		String ret = CommFunc.join3List("-", a, b, c);
		assertEquals("zdh-zdh-zdh;zz-zz-zz;bb-bb-bb",ret);
		ret = CommFunc.join2List("-", b, c);
		assertEquals("zdh-zdh;zz-zz;bb-bb",ret);
	}


}
