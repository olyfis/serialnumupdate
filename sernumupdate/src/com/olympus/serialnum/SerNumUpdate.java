package com.olympus.serialnum;

import javax.servlet.http.HttpServlet;
import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.olympus.olyutil.Olyutil;
import com.olympus.serialnum.sap.SerNumSAP;

@WebServlet("/snupdate")
public class SerNumUpdate extends HttpServlet {
	
	/****************************************************************************************************************************************************/
	public static SerNumSAP doLoadSAPObj(String line, String sep) throws IOException {
		
		SerNumSAP sapObj = new SerNumSAP();
		String[] items = line.split(sep);
	 
		//System.out.println("**** Items=" + items.length + " CN=" + items[0]    + " -- Line=" + line); 
		String componentNumber = items[0];	
		String iBaseNumber = items[1];
		String soldTo = items[2];
		String soldToName = items[3];
		String billTo = items[4];
		String billToName = items[5];
		String shipTo = items[6];
		String shipToName = items[7];
		String sAPMaterial = items[8];
		String equipment = items[9];
		String modelNumber = items[10];
		String serialNumber = items[11];
		String newComponentNumber = items[12];
		String newModeNumber = items[13];
		String newSerialNumber = items[14];
		String processedFlag = "";
		if (items.length > 15 && items[15] != null) {
			processedFlag = items[15];
		 } 
		sapObj.setComponentNumber(componentNumber);
		sapObj.setIBaseNumber(iBaseNumber);
		sapObj.setSoldTo(soldTo);
		sapObj.setSoldToName(soldToName);
		sapObj.setBillTo(billToName);
		sapObj.setBillToName(billToName);
		sapObj.setShipTo(shipTo);
		sapObj.setShipToName(shipToName);
		sapObj.setSAPMaterial(sAPMaterial);
		sapObj.setEquipment(equipment);
		sapObj.setModelNumber(modelNumber);
		sapObj.setSerialNumber(newSerialNumber);
		sapObj.setNewComponentNumber(newComponentNumber);
		sapObj.setNewModeNumber(newModeNumber);
		sapObj.setNewSerialNumber(newSerialNumber);
		sapObj.setProcessedFlag(processedFlag);
		return(sapObj);
	}

		
	/****************************************************************************************************************************************************/
	public static  ArrayList<SerNumSAP> doParseSAPfile( ArrayList<String> strArr, String sep) throws IOException {
		SerNumSAP sapObjRtn = new SerNumSAP();
		ArrayList<SerNumSAP> sapObjArr = new ArrayList<SerNumSAP>();
	 
		for (String str : strArr) {	
			sapObjRtn = doLoadSAPObj(str, sep);
			sapObjArr.add(sapObjRtn);	
		}
		return(sapObjArr);
	}
	/****************************************************************************************************************************************************/
	public static  HashMap<String, Integer>  doBuildSapHash( ArrayList<SerNumSAP> sapObjArr) throws IOException {
		HashMap<String,Integer> hMap = new HashMap<String, Integer>();
		
		String ComponentNumber = "";
		
		for (int i = 0; i < sapObjArr.size(); i++) {
			SerNumSAP sapObj = new SerNumSAP();
			
			sapObj = sapObjArr.get(i);
			ComponentNumber = sapObj.getComponentNumber();
			 hMap.put(ComponentNumber, i);
			//System.out.println("****CN=" + sapObj.getComponentNumber()  + "--");
			
			
		}
		
		
		return(hMap);
	}
	/****************************************************************************************************************************************************/

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<String> strArr = new ArrayList<String>();	
		ArrayList<SerNumSAP> sapObjArrRtn = new ArrayList<SerNumSAP>();
		String sep = ";";
		String snFile = "D:\\Pentaho\\Kettle\\SerNumChk\\serNum.txt";
        String tag = "csvData: ";
        
         //strArr = readFile(snFile);
        strArr = Olyutil.readInputFile(snFile);
     	//Olyutil.printStrArray(strArr);
        sapObjArrRtn = doParseSAPfile( strArr, sep);
        
        System.out.println("CN=" + sapObjArrRtn.get(1).getComponentNumber() + "--"  + "SZ=" + sapObjArrRtn.size());
        System.out.println("NSN=" + sapObjArrRtn.get(1).getNewSerialNumber() + "--"  + "SZ=" + sapObjArrRtn.size());
        
        System.out.println("e=" + sapObjArrRtn.get(1).getEquipment() + "--");
        
        HashMap<String, Integer>  hm = new HashMap<String, Integer>();
        
       hm =  doBuildSapHash(sapObjArrRtn);
       int idx = 0;
       idx = hm.get("11557");
       System.out.println("**E=" + sapObjArrRtn.get(idx).getEquipment() + "--");
     
       idx = hm.get("10369");
       System.out.println("**E=" + sapObjArrRtn.get(idx).getEquipment() + "--");
       
       
	}
		
	/****************************************************************************************************************************************************/

}
