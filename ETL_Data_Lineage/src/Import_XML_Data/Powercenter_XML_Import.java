package Import_XML_Data;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Powercenter_XML_Import {
	
	//private static final char DEFAULT_SEPARATOR 						= ',';
	//private static final String FILE_NAME = "C:/share/MyFirstRE_INSURANCE_LIFE.xls";
	//normal 1 : 1
	//private static final String FILE_NAME 							= "C:/share/MyFirstAlchemy_LIFE.xls";
	private static final String FILE_NAMETEST 							= "C:/share/MyTEST_LIFE.xls";
	private static final int ROW_HEADER_MAPPING_TITLE 					= 1;
	private static final int ROW_HEADER_MAPPING_HEADER_WORKFLOWS 		= 2;
	//private static final String ROW_HEADER_MAPPING_HEADER_WORKFLOWS_STR = "Folder,Workflow,Session,Mapping";
	private static final int ROW_HEADER_MAPPING_DATA_WORKFLOWS 			= 3;
	private static final int ROW_HEADER_MAPPING_OUTLINE 				= 4;
	private static final int ROW_HEADER_MAPPING_PARAMETER_FILE 			= 5;
	private static final int ROW_HEADER_MAPPING_HEADER_SOURCE_TARGET 	= 6;
	//private static final String ROW_HEADER_MAPPING_HEADER_SOURCE_TARGET_STR = "TableName / FileName, Type, Connector, Comments";	
	private static final int ROW_HEADER_MAPPING_SOURCES					= 7;
	private static final int ROW_HEADER_MAPPING_TARGETS 				= 8;
	private static final int ROW_HEADER_MAPPING_JOIN_FILTER_CONDITION 	= 9;
	private static final int ROW_HEADER_MAPPING_JOIN_SOURCE_EXTRACTION 	= 10;
	private static final int ROW_HEADER_MAPPING_PRESQL 					= 11;
	private static final int ROW_HEADER_MAPPING_POSTSQL 				= 12;
	private static final int ROW_HEADER_MAPPING_COMMENT 				= 13;
	private static final int ROW_HEADER_DATA_TITLE 						= 14;

	// not normal 1 : 1 this one has exp outputport has o_ lead in front of name
	
	@SuppressWarnings("deprecation")
	
	public static void main(String argv[]) {
		List<String> ConnectionsFROMFIELD 			= 	new ArrayList<String>();
		List<String> ConnectionsFROMINSTANCE 		= 	new ArrayList<String>();
		List<String> ConnectionsFROMINSTANCETYPE 	= 	new ArrayList<String>();
		List<String> ConnectionsTOFIELD 			= 	new ArrayList<String>();
		List<String> ConnectionsTOINSTANCE 			= 	new ArrayList<String>();
		List<String> ConnectionsTOINSTANCETYPE 		= 	new ArrayList<String>();
		List<String> ConnectionsTOFIELD_PK1 		= 	new ArrayList<String>();
		List<String> ConnectionsTOINSTANCE_PK1 		= 	new ArrayList<String>();
		List<String> ConnectionsTOINSTANCETYPE_PK1 	= 	new ArrayList<String>();
		List<String> ConnectionsTOFIELD_PK2 		= 	new ArrayList<String>();
		List<String> ConnectionsTOINSTANCE_PK2 		= 	new ArrayList<String>();
		List<String> ConnectionsTOINSTANCETYPE_PK2 	= 	new ArrayList<String>();
		List<String> ConnectionsOBJECTPORT 	= 			new ArrayList<String>();
		List<String> ConnectionsOBJECTNAME 	= 			new ArrayList<String>();
		List<String> ConnectionsOBJECTTYPE 	= 			new ArrayList<String>();
		List<String> RepositoryName 		= 			new ArrayList<String>();
		List<String> FolderNAME     		= 			new ArrayList<String>();
		List<String> WorkflowNAME   		= 			new ArrayList<String>();
		List<String> WorkflowDESCRIPTION   	= 			new ArrayList<String>();		
		List<String> SessionNAME    		= 			new ArrayList<String>();
		List<String> MappingNAME    		= 			new ArrayList<String>();
		
		String beginValueConnection = "Source Definition";
		boolean connection_found 	= false;
		boolean all_not_connected 	= false;
		int xml_left_offset 		= 1;
		int xml_top_offset 			= 16;
		int numberOfMappings        = 1;
		//String FILENAMEXML		= "C:/ETL_WORFLOWS/DBM_REINS/wkf_RE_INSURANCE_LIFEJ.XML";
		String FILENAMEXML		= "C:/ETL_WORFLOWS/DBM_AlCHEMY/wkf_Alchemy.XML";
		//String FILENAMEXML		= "C:/ETL_WORFLOWS/MEDSYS/wkf_ETL_LIFE_J_EXTRACT_CSV_DAILY.XML";
		String PARAMETERFILE    = "C:/ETL_WORFLOWS/DBM_ALCHEMY/ALCHEMY.txt";

		try {
			File fXmlFile 						= new File(FILENAMEXML);
			DocumentBuilderFactory dbFactory 	= DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder 			= dbFactory.newDocumentBuilder();
			Document doc 						= dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("REPOSITORY");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					RepositoryName.add(eElement.getAttribute("NAME"));
				}
			}
			//WORKFLOW Parameter Filename
			nList = doc.getElementsByTagName("ATTRIBUTE");
			System.out.println("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;	
					Element mElement = (Element) nNode.getParentNode();	
					if("Parameter Filename".equals(eElement.getAttribute("NAME"))&&("WORKFLOW".equals(nNode.getParentNode().getNodeName()))){
						System.out.println("Mapping Name :"  + nNode.getParentNode().getNodeName() + " : " + mElement.getAttribute("NAME"));
						System.out.println("Current Element :" + eElement.getAttribute("NAME"));
						System.out.println("INSTANCE DBDNAME :" + eElement.getAttribute("VALUE"));
					}
				}
			}
			nList = doc.getElementsByTagName("FOLDER");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;					
					FolderNAME.add(eElement.getAttribute("NAME"));
				}
			}
			nList = doc.getElementsByTagName("WORKFLOW");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;					
					WorkflowNAME.add(eElement.getAttribute("NAME"));
					WorkflowDESCRIPTION.add(eElement.getAttribute("DESCRIPTION"));
				}
			}					
			nList = doc.getElementsByTagName("SESSION");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;					
					SessionNAME.add(eElement.getAttribute("NAME"));
					MappingNAME.add(eElement.getAttribute("MAPPINGNAME"));
				}
			}
			numberOfMappings = MappingNAME.size()-1;
			nList = doc.getElementsByTagName("CONNECTOR");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
//-----------------------------------------------------------------------------
//				load super table 	
//				get the connection between all objects
//-----------------------------------------------------------------------------							
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Element mElement = (Element) nNode.getParentNode();	
					MappingNAME.add(mElement.getAttribute("NAME"));
					ConnectionsFROMFIELD.add(eElement.getAttribute("FROMFIELD"));
					ConnectionsFROMINSTANCE.add(eElement.getAttribute("FROMINSTANCE"));
					ConnectionsFROMINSTANCETYPE.add(eElement.getAttribute("FROMINSTANCETYPE"));
					ConnectionsTOFIELD.add(eElement.getAttribute("TOFIELD"));
					ConnectionsTOINSTANCE.add(eElement.getAttribute("TOINSTANCE"));
					ConnectionsTOINSTANCETYPE.add(eElement.getAttribute("TOINSTANCETYPE"));
				}
			}
		    } catch (Exception e) {e.printStackTrace();
		    }
//-----------------------------------------------------------------------------
//			start of Recursive data		
//		    Get the first object "Source Definition" , 
//			Get the connections of the Start object to the next object.
//-----------------------------------------------------------------------------			
			String ConnectionINSTANCETYPE = beginValueConnection;
			int counter01 = xml_left_offset;
			int counter02 = xml_top_offset;
			int excelcounter01 = xml_top_offset;
			for (int i = 0; i < ConnectionsFROMFIELD.size(); i++) {
				if (ConnectionsFROMINSTANCETYPE.get(i).equals(ConnectionINSTANCETYPE)){
					counter02=counter02+1;
					excelcounter01 = excelcounter01 + 1;
					ConnectionsTOFIELD_PK1.add(ConnectionsTOFIELD.get(i));
					ConnectionsTOINSTANCE_PK1.add(ConnectionsTOINSTANCE.get(i));
					ConnectionsTOINSTANCETYPE_PK1.add(ConnectionsTOINSTANCETYPE.get(i));
					ConnectionsOBJECTPORT.add("" + counter01);	
					ConnectionsOBJECTPORT.add(""+excelcounter01);	
					ConnectionsOBJECTPORT.add(ConnectionsFROMFIELD.get(i));
					ConnectionsOBJECTNAME.add("" + (counter01 + 1));					
					ConnectionsOBJECTNAME.add(""+excelcounter01);					
					ConnectionsOBJECTNAME.add(ConnectionsFROMINSTANCE.get(i));
					ConnectionsOBJECTTYPE.add("" + (counter01 + 2));					
					ConnectionsOBJECTTYPE.add(""+excelcounter01);					
					ConnectionsOBJECTTYPE.add(ConnectionsFROMINSTANCETYPE.get(i));
					}
			}
//-----------------------------------------------------------------------------
//			Recursive loop			
//			Get the connections following the data lineage for start object. 
//          added "o_" pre_string (ignore) when port name change inside of object.
//-----------------------------------------------------------------------------
while (true){	
	    counter02 = xml_top_offset;
		counter01=counter01+3;
		excelcounter01 = xml_top_offset;
				for (int l = 0; l < ConnectionsTOFIELD_PK1.size(); l++) {
				for (int k = 0; k < ConnectionsTOFIELD.size(); k++) {
					// remove the pre_Strings 
					//**************************************************
					String FieldNameTo = (ConnectionsTOFIELD_PK1.get(l));
					String FieldNameFrom = (ConnectionsFROMFIELD.get(k));
					if ("i_".equals(FieldNameTo.substring(0,2))){
						FieldNameTo=FieldNameTo.substring(2);
					} 
					if ("o_".equals(FieldNameFrom.substring(0,2))){
						FieldNameFrom=FieldNameFrom.substring(2);
					}
					//**************************************************
					if ((FieldNameFrom.equals(FieldNameTo)) &&
						 ConnectionsFROMINSTANCE.get(k).equals(ConnectionsTOINSTANCE_PK1.get(l)) &&
						 ConnectionsFROMINSTANCETYPE.get(k).equals(ConnectionsTOINSTANCETYPE_PK1.get(l)))
					    {
						counter02=counter02+1;
						excelcounter01 = excelcounter01 + 1;
						ConnectionsTOFIELD_PK2.add(ConnectionsTOFIELD.get(k));
						ConnectionsTOINSTANCE_PK2.add(ConnectionsTOINSTANCE.get(k));
						ConnectionsTOINSTANCETYPE_PK2.add(ConnectionsTOINSTANCETYPE.get(k));
						ConnectionsOBJECTPORT.add(""+counter01);		
						ConnectionsOBJECTPORT.add(""+excelcounter01);							
						ConnectionsOBJECTPORT.add(ConnectionsFROMFIELD.get(k));				
						ConnectionsOBJECTNAME.add(""+(counter01+1));
						ConnectionsOBJECTNAME.add(""+excelcounter01);
						ConnectionsOBJECTNAME.add(ConnectionsFROMINSTANCE.get(k));
						ConnectionsOBJECTTYPE.add(""+(counter01+2));
						ConnectionsOBJECTTYPE.add(""+excelcounter01);
						ConnectionsOBJECTTYPE.add(ConnectionsFROMINSTANCETYPE.get(k));
						connection_found = true;
					}
				}
				
				
				if(!connection_found)
				{
					counter02=counter02+1;
					excelcounter01 = excelcounter01 + 1;
					ConnectionsOBJECTPORT.add(""+(counter01));
					ConnectionsOBJECTPORT.add(""+excelcounter01);
					ConnectionsOBJECTPORT.add(ConnectionsTOFIELD_PK1.get(l));					
					ConnectionsOBJECTNAME.add(""+(counter01+1));
					ConnectionsOBJECTNAME.add(""+excelcounter01);
					ConnectionsOBJECTNAME.add(ConnectionsTOINSTANCE_PK1.get(l));
					ConnectionsOBJECTTYPE.add(""+(counter01+2));	
					ConnectionsOBJECTTYPE.add(""+excelcounter01);	
					ConnectionsOBJECTTYPE.add(ConnectionsTOINSTANCETYPE_PK1.get(l));
					ConnectionsTOFIELD_PK2.add("NOT CONNECTED");
					ConnectionsTOINSTANCE_PK2.add("NOT CONNECTED");
					ConnectionsTOINSTANCETYPE_PK2.add("NOT CONNECTED");
				}
				connection_found = false;
			}	
			ConnectionsTOFIELD_PK1.clear();
			ConnectionsTOINSTANCE_PK1.clear();
			ConnectionsTOINSTANCETYPE_PK1.clear();
			for (int c = 0; c < ConnectionsTOFIELD_PK2.size(); c++) {
				ConnectionsTOFIELD_PK1.add(ConnectionsTOFIELD_PK2.get(c));
				ConnectionsTOINSTANCE_PK1.add(ConnectionsTOINSTANCE_PK2.get(c));
				ConnectionsTOINSTANCETYPE_PK1.add(ConnectionsTOINSTANCETYPE_PK2.get(c));
			};
			ConnectionsTOFIELD_PK2.clear();
			ConnectionsTOINSTANCE_PK2.clear();
			ConnectionsTOINSTANCETYPE_PK2.clear();
			all_not_connected = false;
			int occurrences   = Collections.frequency(ConnectionsTOFIELD_PK1, "NOT CONNECTED");
			int allconnectors = ConnectionsTOFIELD_PK1.size();
			if(occurrences == allconnectors){all_not_connected = true;}
			if(all_not_connected){break;}
	} // While
//-----------------------------------------------------------------------------
//				EINDE Recursive loop			
//-----------------------------------------------------------------------------
		HSSFWorkbook workbooktest 	= new HSSFWorkbook();
        HSSFSheet sheettest 		= workbooktest.createSheet("Datatypes in Java");
        int MaxRowNumber 	= 0;
        for (int i = 0; i < ConnectionsOBJECTPORT.size(); i=i+3) {
        	if (Integer.parseInt(ConnectionsOBJECTPORT.get(i+1)) > MaxRowNumber) {MaxRowNumber = Integer.parseInt(ConnectionsOBJECTPORT.get(i+1));};
        	if (Integer.parseInt(ConnectionsOBJECTNAME.get(i+1)) > MaxRowNumber) {MaxRowNumber = Integer.parseInt(ConnectionsOBJECTNAME.get(i+1));};
        	if (Integer.parseInt(ConnectionsOBJECTTYPE.get(i+1)) > MaxRowNumber) {MaxRowNumber = Integer.parseInt(ConnectionsOBJECTTYPE.get(i+1));};
        }
        MaxRowNumber		= MaxRowNumber+1; 
        int MaxColumnCheck 	= 0;
        for (int i = 0; i < ConnectionsOBJECTPORT.size(); i=i+3) {
        	if (Integer.parseInt(ConnectionsOBJECTPORT.get(i)) > MaxColumnCheck) {MaxColumnCheck = Integer.parseInt(ConnectionsOBJECTPORT.get(i));};
        	if (Integer.parseInt(ConnectionsOBJECTNAME.get(i)) > MaxColumnCheck) {MaxColumnCheck = Integer.parseInt(ConnectionsOBJECTNAME.get(i));};
        	if (Integer.parseInt(ConnectionsOBJECTTYPE.get(i)) > MaxColumnCheck) {MaxColumnCheck = Integer.parseInt(ConnectionsOBJECTTYPE.get(i));};
        }
        int MaxColumnNumber	= MaxColumnCheck;
        if (MaxColumnNumber<11){MaxColumnNumber = 12;}
        int one_fourt_div 		= ((MaxColumnNumber-((MaxColumnNumber-1) % 4))/4);
        int one_fourt_rest 		= ((MaxColumnNumber-1) % 4) ;

        CellStyle stylenormal 	= workbooktest.createCellStyle();
			stylenormal 		= workbooktest.createCellStyle();
			stylenormal.setBorderBottom(CellStyle.BORDER_THIN);
			stylenormal.setBorderTop(CellStyle.BORDER_THIN);
			stylenormal.setBorderRight(CellStyle.BORDER_THIN);
			stylenormal.setBorderLeft(CellStyle.BORDER_THIN);
        
		CellStyle styletest 	= workbooktest.createCellStyle();
			styletest 			= workbooktest.createCellStyle();
			styletest.setFillForegroundColor(IndexedColors.RED.getIndex());
			styletest.setFillPattern(CellStyle.SOLID_FOREGROUND);	
			styletest.setBorderBottom(CellStyle.BORDER_THIN);
			styletest.setBorderTop(CellStyle.BORDER_THIN);
			styletest.setBorderRight(CellStyle.BORDER_THIN);
			styletest.setBorderLeft(CellStyle.BORDER_THIN);
	    
	    CellStyle stylecenterSH = workbooktest.createCellStyle();
	    	stylecenterSH.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    	stylecenterSH.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    	stylecenterSH.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
	    	stylecenterSH.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    	stylecenterSH.setBorderBottom(CellStyle.BORDER_THIN);
	    	stylecenterSH.setBorderTop(CellStyle.BORDER_THIN);
	    	stylecenterSH.setBorderRight(CellStyle.BORDER_THIN);
	    	stylecenterSH.setBorderLeft(CellStyle.BORDER_THIN);

	    CellStyle stylecenter 	= workbooktest.createCellStyle();
	    	stylecenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    	stylecenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    	stylecenter.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
	    	stylecenter.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    	stylecenter.setBorderBottom(CellStyle.BORDER_THIN);
	    	stylecenter.setBorderTop(CellStyle.BORDER_THIN);
	    	stylecenter.setBorderRight(CellStyle.BORDER_THIN);
	    	stylecenter.setBorderLeft(CellStyle.BORDER_THIN);
	    	Font font_header 	= workbooktest.createFont();
	    		font_header.setFontHeightInPoints((short)12);
	    		font_header.setFontName("Arial");
	    	stylecenter.setFont(font_header);

	    CellStyle style_Data_Header	= workbooktest.createCellStyle();
	    	style_Data_Header.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    	style_Data_Header.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    	style_Data_Header.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
	    	style_Data_Header.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    	style_Data_Header.setBorderBottom(CellStyle.BORDER_THIN);
	    	style_Data_Header.setBorderTop(CellStyle.BORDER_THIN);
	    	style_Data_Header.setBorderRight(CellStyle.BORDER_THIN);
	    	style_Data_Header.setBorderLeft(CellStyle.BORDER_THIN);
	    	Font font_Data_Header 	= workbooktest.createFont();
	    		font_Data_Header.setFontHeightInPoints((short)12);
	    		font_Data_Header.setFontName("Arial");
	    	style_Data_Header.setFont(font_Data_Header);	    
	    
	    
	    CellStyle style_title 	= workbooktest.createCellStyle();
	    	style_title.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    	style_title.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    	style_title.setBorderBottom(CellStyle.BORDER_THIN);
	    	style_title.setBorderTop(CellStyle.BORDER_THIN);
	    	style_title.setBorderRight(CellStyle.BORDER_THIN);
	    	style_title.setBorderLeft(CellStyle.BORDER_THIN);
	    	Font font_title 	= workbooktest.createFont();
	    		font_title.setFontHeightInPoints((short)12);
	    		font_title.setFontName("Arial");
	    	style_title.setFont(font_header);
	    
	    CellStyle stylesubheader = workbooktest.createCellStyle();
	    	stylesubheader 		 = workbooktest.createCellStyle();
	    	stylesubheader.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
	    	stylesubheader.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    	Font font_subheader  = workbooktest.createFont();
	    		font_subheader.setFontHeightInPoints((short)8);
	    		font_subheader.setFontName("Arial");
	    		font_subheader.setBold(true);
	    	stylesubheader.setFont(font_subheader);
	    
        for (int excelRowNUM = 0; excelRowNUM < MaxRowNumber; excelRowNUM++) {
        	Row rowtest = sheettest.createRow(excelRowNUM);
        	for (int i = 0; i < ConnectionsOBJECTPORT.size(); i=i+3) {
        		
         		//Header excel page
        		if (excelRowNUM == ROW_HEADER_MAPPING_TITLE && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,MaxColumnNumber));
        			Cell celltest = rowtest.createCell(excelRowNUM);
        			celltest.setCellValue(FolderNAME.get(0));
        			celltest.setCellStyle(style_title);
        		}
        		if (excelRowNUM == ROW_HEADER_MAPPING_HEADER_WORKFLOWS  && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,one_fourt_div+xml_left_offset));
        			Cell celltest1 = rowtest.createCell(1);
        			celltest1.setCellValue("Folder");
        			celltest1.setCellStyle(stylecenter);
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,one_fourt_div+xml_left_offset+1,((one_fourt_div)*2)+xml_left_offset+1));
        			Cell celltest2 = rowtest.createCell(4);
        			celltest2.setCellValue("Workflow");
        			celltest2.setCellStyle(stylecenter);
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,((one_fourt_div*2)+xml_left_offset+2),(one_fourt_div*3)+xml_left_offset+2));
        			Cell celltest3 = rowtest.createCell(7);
        			celltest3.setCellValue("Session");
        			celltest3.setCellStyle(stylecenter);
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,((one_fourt_div*3)+xml_left_offset+3),(one_fourt_div*4)+xml_left_offset+one_fourt_rest));
        			Cell celltest4 = rowtest.createCell(10);
        			celltest4.setCellValue("Mapping");
        			celltest4.setCellStyle(stylecenter);
        		}        		
        		if (excelRowNUM == ROW_HEADER_MAPPING_DATA_WORKFLOWS && i == 0 ){
        			// loop for number of mappings extenal loop in before loop ??
        			//int mappingloop = 0;
        			excelRowNUM = excelRowNUM - 1;
        			for(int mappingloop = 0; mappingloop < numberOfMappings+1 ; mappingloop++){ 
        				excelRowNUM = excelRowNUM + 1;
        				System.out.println("mapping loop number : " + excelRowNUM );
        				System.out.println("session : "             + SessionNAME.get(mappingloop));
        				System.out.println("mappings : "            + MappingNAME.get(mappingloop));
        				rowtest = sheettest.createRow(excelRowNUM);
        				
        				sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,one_fourt_div+xml_left_offset));
        				Cell celltest1 = rowtest.createCell(1);
        				celltest1.setCellValue(FolderNAME.get(0));
        				System.out.println("1 "+excelRowNUM + FolderNAME.get(0));
        				
        				sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,one_fourt_div+xml_left_offset+1,((one_fourt_div)*2)+xml_left_offset+1));
        				Cell celltest2 = rowtest.createCell(4);
        				celltest2.setCellValue(WorkflowNAME.get(0));
        				System.out.println("2 "+excelRowNUM + WorkflowNAME.get(0));
        				
        				sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,((one_fourt_div*2)+xml_left_offset+2),(one_fourt_div*3)+xml_left_offset+2));
        				Cell celltest3 = rowtest.createCell(7);
        				celltest3.setCellValue(SessionNAME.get(mappingloop));
        				System.out.println("3 "+excelRowNUM + SessionNAME.get(mappingloop));
        				
        				sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,((one_fourt_div*3)+xml_left_offset+3),(one_fourt_div*4)+xml_left_offset+one_fourt_rest));
        				Cell celltest4 = rowtest.createCell(10);
        				celltest4.setCellValue(MappingNAME.get(mappingloop));
        				System.out.println("4 "+excelRowNUM + MappingNAME.get(mappingloop));
        			}
        			       			
        		} 
        		if (excelRowNUM == ROW_HEADER_MAPPING_OUTLINE + numberOfMappings && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,one_fourt_div+xml_left_offset));
        			Cell celltest1 = rowtest.createCell(1);
        			celltest1.setCellValue("Outline");
        			celltest1.setCellStyle(stylesubheader);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,(one_fourt_div+xml_left_offset+1),(one_fourt_div*4)+xml_left_offset+one_fourt_rest));
        			Cell celltest4 = rowtest.createCell(4);
        			celltest4.setCellValue(WorkflowDESCRIPTION.get(0));
        		} 
        		if (excelRowNUM == ROW_HEADER_MAPPING_PARAMETER_FILE + numberOfMappings && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,one_fourt_div+xml_left_offset));
        			Cell celltest1 = rowtest.createCell(1);
        			celltest1.setCellValue("Parameter File");
        			celltest1.setCellStyle(stylesubheader);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,(one_fourt_div+xml_left_offset+1),(one_fourt_div*4)+xml_left_offset+one_fourt_rest));
        			Cell celltest4 = rowtest.createCell(4);
        			celltest4.setCellValue("Place of parameterfile");       			
        		} 
        		if (excelRowNUM == ROW_HEADER_MAPPING_HEADER_SOURCE_TARGET + numberOfMappings && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,one_fourt_div+xml_left_offset));
        			Cell celltest1 = rowtest.createCell(1);
        			celltest1.setCellValue("");
        			celltest1.setCellStyle(stylesubheader);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,one_fourt_div+xml_left_offset+1,((one_fourt_div)*2)+xml_left_offset+1));
        			Cell celltest2 = rowtest.createCell(4);
        			celltest2.setCellValue("TableName / FileName");
 
        			//sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,((one_fourt_div*2)+xml_left_offset+2),(one_fourt_div*2)+xml_left_offset+2));
        			Cell celltest3 = rowtest.createCell(7);
        			celltest3.setCellValue("Type");
        			celltest3.setCellStyle(stylenormal);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,((one_fourt_div*2)+xml_left_offset+3),(one_fourt_div*3)+xml_left_offset+2));
        			Cell celltest4 = rowtest.createCell(8);
        			celltest4.setCellValue("Connector");
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,((one_fourt_div*3)+xml_left_offset+3),(one_fourt_div*4)+xml_left_offset+one_fourt_rest));
        			Cell celltest5 = rowtest.createCell(10);
        			celltest5.setCellValue("Comments");        			
        		} 
        		if (excelRowNUM == ROW_HEADER_MAPPING_SOURCES + numberOfMappings && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,one_fourt_div+xml_left_offset));
        			Cell celltest1 = rowtest.createCell(1);
        			celltest1.setCellValue("Source : ");
        			celltest1.setCellStyle(stylesubheader);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,one_fourt_div+xml_left_offset+1,((one_fourt_div)*2)+xml_left_offset+1));
        			Cell celltest2 = rowtest.createCell(4);
        			celltest2.setCellValue("TableName / FileName");
 
        			Cell celltest3 = rowtest.createCell(7);
        			celltest3.setCellValue("Type");
        			celltest3.setCellStyle(stylenormal);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,((one_fourt_div*2)+xml_left_offset+3),(one_fourt_div*3)+xml_left_offset+2));
        			Cell celltest4 = rowtest.createCell(8);
        			celltest4.setCellValue("Connector");
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,((one_fourt_div*3)+xml_left_offset+3),(one_fourt_div*4)+xml_left_offset+one_fourt_rest));
        			Cell celltest5 = rowtest.createCell(10);
        			celltest5.setCellValue("Comments");          			
        		} 
        		if (excelRowNUM == ROW_HEADER_MAPPING_TARGETS + numberOfMappings && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,one_fourt_div+xml_left_offset));
        			Cell celltest1 = rowtest.createCell(1);
        			celltest1.setCellValue("Target : ");
        			celltest1.setCellStyle(stylesubheader);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,one_fourt_div+xml_left_offset+1,((one_fourt_div)*2)+xml_left_offset+1));
        			Cell celltest2 = rowtest.createCell(4);
        			celltest2.setCellValue("TableName / FileName");
 
        			Cell celltest3 = rowtest.createCell(7);
        			celltest3.setCellValue("Type");
        			celltest3.setCellStyle(stylenormal);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,((one_fourt_div*2)+xml_left_offset+3),(one_fourt_div*3)+xml_left_offset+2));
        			Cell celltest4 = rowtest.createCell(8);
        			celltest4.setCellValue("Connector");
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,((one_fourt_div*3)+xml_left_offset+3),(one_fourt_div*4)+xml_left_offset+one_fourt_rest));
        			Cell celltest5 = rowtest.createCell(10);
        			celltest5.setCellValue("Comments");         			
        		} 
        		if (excelRowNUM == ROW_HEADER_MAPPING_JOIN_FILTER_CONDITION + numberOfMappings && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,one_fourt_div+xml_left_offset));
        			Cell celltest1 = rowtest.createCell(1);
        			celltest1.setCellValue("Join/Filter condition");
        			celltest1.setCellStyle(stylesubheader);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,(one_fourt_div+xml_left_offset+1),(one_fourt_div*4)+xml_left_offset+one_fourt_rest));
        			Cell celltest4 = rowtest.createCell(4);
        			celltest4.setCellValue("SQL code");    			
        		} 
        		if (excelRowNUM == ROW_HEADER_MAPPING_JOIN_SOURCE_EXTRACTION + numberOfMappings && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,one_fourt_div+xml_left_offset));
        			Cell celltest1 = rowtest.createCell(1);
        			celltest1.setCellValue("Source Extraction SQL");
        			celltest1.setCellStyle(stylesubheader);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,(one_fourt_div+xml_left_offset+1),(one_fourt_div*4)+xml_left_offset+one_fourt_rest));
        			Cell celltest4 = rowtest.createCell(4);
        			celltest4.setCellValue("SQL code");    			
        		} 
        		if (excelRowNUM == ROW_HEADER_MAPPING_PRESQL + numberOfMappings && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,one_fourt_div+xml_left_offset));
        			Cell celltest1 = rowtest.createCell(1);
        			celltest1.setCellValue("ETL Pre-session SQL");
        			celltest1.setCellStyle(stylesubheader);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,(one_fourt_div+xml_left_offset+1),(one_fourt_div*4)+xml_left_offset+one_fourt_rest));
        			Cell celltest4 = rowtest.createCell(4);
        			celltest4.setCellValue("SQL code");    			        			
        		} 
        		if (excelRowNUM == ROW_HEADER_MAPPING_POSTSQL + numberOfMappings && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,one_fourt_div+xml_left_offset));
        			Cell celltest1 = rowtest.createCell(1);
        			celltest1.setCellValue("ETL post-session SQL");
        			celltest1.setCellStyle(stylesubheader);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,(one_fourt_div+xml_left_offset+1),(one_fourt_div*4)+xml_left_offset+one_fourt_rest));
        			Cell celltest4 = rowtest.createCell(4);
        			celltest4.setCellValue("SQL code");         			
        		} 
        		if (excelRowNUM == ROW_HEADER_MAPPING_COMMENT + numberOfMappings && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,one_fourt_div+xml_left_offset));
        			Cell celltest1 = rowtest.createCell(1);
        			celltest1.setCellValue("comment");
        			celltest1.setCellStyle(stylesubheader);
        			
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,(one_fourt_div+xml_left_offset+1),(one_fourt_div*4)+xml_left_offset+one_fourt_rest));
        			Cell celltest4 = rowtest.createCell(4);
        			celltest4.setCellValue("text  ");         			
        		} 

        		if (excelRowNUM == ROW_HEADER_DATA_TITLE + numberOfMappings && i == 0 ){
        			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,xml_left_offset,MaxColumnNumber));
        			Cell celltest5 = rowtest.createCell(1);
        			celltest5.setCellValue("Mapping header");
        			celltest5.setCellStyle(style_Data_Header);
        		}

        		if (excelRowNUM == 15 + numberOfMappings && i == 0){
        			for (int loop = 0; loop < MaxColumnCheck ; loop=loop+3){
            			sheettest.addMergedRegion(new CellRangeAddress(excelRowNUM,excelRowNUM,loop+xml_left_offset,loop+2+xml_left_offset));
            			Cell celltest1 = rowtest.createCell(1+loop);
            			celltest1.setCellValue("Object");
            			celltest1.setCellStyle(stylecenterSH);
        			}
        		}

        		if (excelRowNUM == 16 + numberOfMappings  ){
         			if (excelRowNUM+1 == Integer.parseInt(ConnectionsOBJECTPORT.get(i+1))){
        				//System.out.println("row number : " + excelRowNUM);
        				Cell celltest = rowtest.createCell(Integer.parseInt(ConnectionsOBJECTPORT.get(i)));
        				celltest.setCellValue("Field Name");
        				celltest.setCellStyle(style_Data_Header);
        			}
        			if (excelRowNUM+1 == Integer.parseInt(ConnectionsOBJECTNAME.get(i+1))){
        				Cell celltest = rowtest.createCell(Integer.parseInt(ConnectionsOBJECTNAME.get(i)));
        				celltest.setCellValue("Naam Object");
        				celltest.setCellStyle(style_Data_Header);
        			}
        			if (excelRowNUM+1 == Integer.parseInt(ConnectionsOBJECTTYPE.get(i+1))){
        				Cell celltest = rowtest.createCell(Integer.parseInt(ConnectionsOBJECTTYPE.get(i)));
        				celltest.setCellValue("Type object");
        				celltest.setCellStyle(style_Data_Header);
        			}
        		}

        		
        		//Data mapping lineage 
        		//get the connector data from the recursive loop and create a table in the Excel sheet
        			if (excelRowNUM == Integer.parseInt(ConnectionsOBJECTPORT.get(i+1))){
        				Cell celltest = rowtest.createCell(Integer.parseInt(ConnectionsOBJECTPORT.get(i)));
        				celltest.setCellValue(ConnectionsOBJECTPORT.get(i+2));
        				celltest.setCellStyle(stylenormal);
        				if ("NOT CONNECTED".equals(ConnectionsOBJECTPORT.get(i+2))){celltest.setCellStyle(styletest);}
        			}
        			if (excelRowNUM == Integer.parseInt(ConnectionsOBJECTNAME.get(i+1))){
        				Cell celltest = rowtest.createCell(Integer.parseInt(ConnectionsOBJECTNAME.get(i)));
        				celltest.setCellValue(ConnectionsOBJECTNAME.get(i+2));
        				celltest.setCellStyle(stylenormal);
        				if ("NOT CONNECTED".equals(ConnectionsOBJECTPORT.get(i+2))){celltest.setCellStyle(styletest);}
        			}
        			if (excelRowNUM == Integer.parseInt(ConnectionsOBJECTTYPE.get(i+1))){
        				Cell celltest = rowtest.createCell(Integer.parseInt(ConnectionsOBJECTTYPE.get(i)));
        				celltest.setCellValue(ConnectionsOBJECTTYPE.get(i+2));
        				celltest.setCellStyle(stylenormal);
        				if ("NOT CONNECTED".equals(ConnectionsOBJECTPORT.get(i+2))){celltest.setCellStyle(styletest);}
        			}
           	}
        }
		try {
			setBordersToMergedCells(workbooktest, sheettest);
		    FileOutputStream outputStreamtest = new FileOutputStream(FILE_NAMETEST);
			for (int i = 0; i < sheettest.getRow(sheettest.getLastRowNum()).getPhysicalNumberOfCells()+8 ; ++i) {
				sheettest.autoSizeColumn(i);
			    }
			workbooktest.write(outputStreamtest); 
		    workbooktest.close();
		} catch (FileNotFoundException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();}
   } //main function
	
	@SuppressWarnings("deprecation")
	private static void setBordersToMergedCells(HSSFWorkbook workBook, HSSFSheet sheet) {
		  List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
		  for (CellRangeAddress rangeAddress : mergedRegions) {
		    RegionUtil.setBorderTop(CellStyle.BORDER_THIN,    rangeAddress, sheet, workBook);
		    RegionUtil.setBorderLeft(CellStyle.BORDER_THIN,   rangeAddress, sheet, workBook);
		    RegionUtil.setBorderRight(CellStyle.BORDER_THIN,  rangeAddress, sheet, workBook);
		    RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, rangeAddress, sheet, workBook);
		  }
		}
	
} //class