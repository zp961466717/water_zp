package hello;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins="*",maxAge=3600)
@RestController
public class GreetingController {
	boolean ii = false;
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template,name));
    }
    @RequestMapping("/HELLO")
    @ResponseBody
    public String hello() {
    	System.out.println("hhh");
    	return "ff";
    }
    public  static byte []  itob(int  [] intarr)  
    {  
    	
          byte[] bt=new byte[intarr.length];
          for (int i = 0; i < intarr.length; i++) {
			bt[i]=(byte)((intarr[i])& 0b1111_1111);
		}
        return bt;  
    }
    
   
	@GetMapping("/checkwater")
    public Object checkwater(int id)   {
		Map<String, Object> mod=new HashMap<>();
    	if (id==1) {
        	ContinueRead cRead = new ContinueRead();
        	String str1s;
        	int i = cRead.startComPort();
            String[] sty= {};
            OutputStream outputStream =ContinueRead.outputStream;
         /*   List<Dataget> list=new ArrayList();
            list.add( new Dataget("0004.00","2018-06-24 08:37:46 星期日"));
        	mod.put("list", list);
        	return mod;*/
            //ID为1时是获取最新数据
            List<Dataget> list=new ArrayList();
            if (i == 1) {
              
                cRead.start();
                try {
                    	String st = "FE FE 68 10 65 12 12 16 00 99 15 01 03 90 1F 01 79 16";
                    	
                	//byte[] btes= new byte{FE FE 68 10 65 12 12 16 00 99 15 01 03 90 1F 01 79 16};
                    	
                    sty=st.split(" ");
                    int[] ins=new int[sty.length];
                	byte[] bty=new byte[sty.length];
                    for(int k=0;k<sty.length;k++) {
                    	ins[k]=Integer.parseInt(sty[k],16);
                    			//System.out.println(ins[k]);
                    }
                    bty= itob(ins);
                    for (int j = 0; j < bty.length; j++) {
                    	outputStream.write(bty[j]);
    				}
                    int k=0;
                    do {
                    	str1s=cRead.waternums;
                    	k++;
                    	Thread.sleep(1000);
                    	if(k>10) {
                    		break;
                    	}
					} while (str1s==null);        
					//System.out.println(cRead.waternums);
                    if(str1s!=null) {
                    	String[] ff=str1s.split("----");
                    	list.add( new Dataget("2018-06-24 08:37:46 星期日","0004.00"));
                    	mod.put("list", list);
                    	return mod;
                    }else {
                    	return null;
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	}
		} 
    	else if(id==2)
    	{
			Txt file =new Txt();
			ArrayList lists =new ArrayList();
			List<Dataget> list=new ArrayList();
			String str ;
			StringBuffer strs = new  StringBuffer()  ;
			try {
				lists=file.read_me("E:\\dd.txt");
				for (int i = 0; i < lists.size(); i++) {
					str= lists.get(i).toString();
					String[] ff=str.split("----");
					list.add( new Dataget(ff[1],ff[0]));
				}
				mod.put("list",list);
				System.out.println(strs);
				  return mod;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
				// TODO Auto-generated catch block
		}
		return null;
}
}
