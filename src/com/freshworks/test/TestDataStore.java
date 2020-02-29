package com.freshworks.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.freshworks.constants.FreshworksConstants;
import com.freshworks.datastore.DataStore;

public class TestDataStore {

	private DataStore dataStore;
	
	private static FreshworksConstants constants = new FreshworksConstants();
	
	@SuppressWarnings("static-access")
	@Test
	public void testFilePath() {
		
		long start = System.currentTimeMillis();
		
		//Check for Truthy
		boolean resultTrue = dataStore.isFilePathValid(constants.getKeyStorePath());
		System.out.println("Result1:"+resultTrue);
		
		//Check for Falsy
		boolean resultFalse = dataStore.isFilePathValid(constants.INVALID_FILE_PATH);
		System.out.println("Result2:"+resultFalse);
		
		long end = System.currentTimeMillis();
		long runTime = end-start;
		System.out.println("Execution Time in Milliseconds: "+ runTime);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testSetFixedFilePath() {
		
		long start = System.currentTimeMillis();
		
		//Check for Default Location
		String resultForDefaultLocation = dataStore.setFixedFilePath("");
		System.out.println("Default Location:"+resultForDefaultLocation);
		
		//Check for Chosen Location
		String resultForUserChosenLocation = dataStore.setFixedFilePath(System.getProperty("user.home")+"\\Desktop");
		System.out.println("Chosen Location:"+resultForUserChosenLocation);
		
		long end = System.currentTimeMillis();
		long runTime = end-start;
		System.out.println("Execution Time in Milliseconds: "+ runTime);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testCheckIfValidOperation() {
		
		long start = System.currentTimeMillis();
		
		//Check for Create Operation
		boolean createOperation = dataStore.checkIfValidOperation("C");
		System.out.println("Create Operation Possible?:"+createOperation);
		
		//Check for Read Operation
		boolean readOperation = dataStore.checkIfValidOperation("R");
		System.out.println("Read Operation Possible?:"+readOperation);
		
		//Check for Delete Operation
		boolean deleteOperation = dataStore.checkIfValidOperation("D");
		System.out.println("Delete Operation Possible?:"+deleteOperation);
		
		//Check for Create Operation
		boolean createOperationLowercase = dataStore.checkIfValidOperation("c");
		System.out.println("Create Operation Lowercase Possible?:"+createOperationLowercase);
		
		//Check for Read Operation
		boolean readOperationLowercase = dataStore.checkIfValidOperation("r");
		System.out.println("Read Operation Lowercase Possible?:"+readOperationLowercase);
		
		//Check for Delete Operation
		boolean deleteOperationLowercase = dataStore.checkIfValidOperation("d");
		System.out.println("Delete Operation Lowercase Possible?:"+deleteOperationLowercase);
		
		//Check for Delete Operation
		boolean invalidOperations = dataStore.checkIfValidOperation("U");
		System.out.println("Invalid Operation Possible?:"+invalidOperations);
		
		long end = System.currentTimeMillis();
		long runTime = end-start;
		System.out.println("Execution Time in Milliseconds: "+ runTime);
	}
	
	@SuppressWarnings({ "static-access" })
	@Test
	public void testCreateEntry(){
		File file = new File(constants.getKeyStorePath());			
		try {
			System.out.print(dataStore.createEntry(file,""));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testReadEntry() {
		
		long start = System.currentTimeMillis();
		
		System.out.println(dataStore.readEntry("123"));
		
		long end = System.currentTimeMillis();
		long runTime = end-start;
		System.out.println("Execution Time in Milliseconds: "+ runTime);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testDeleteEntry() {
		
		long start = System.currentTimeMillis();
		
		System.out.println(dataStore.deleteEntry("1000"));
		
		long end = System.currentTimeMillis();
		long runTime = end-start;
		System.out.println("Execution Time in Milliseconds: "+ runTime);
	}

	@SuppressWarnings("static-access")
	@Test
	public void testReadJson() {
		
		long start = System.currentTimeMillis();
		
		try {
			System.out.println(dataStore.readJson(constants.getJsonPath()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		long runTime = end-start;
		System.out.println("Execution Time in Milliseconds: "+ runTime);
	}
	
	@SuppressWarnings("static-access")
	@Test 
	public void testValidSizeOfJson() {
		
		long start = System.currentTimeMillis();
		
		try {
			boolean result = dataStore.validateSizeOfJson(constants.getJsonPath());
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		long runTime = end-start;
		System.out.println("Execution Time in Milliseconds: "+ runTime);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testGetTimeToLiveProperty() {
		
		long start = System.currentTimeMillis();
		
		try {
			System.out.println(dataStore.getTimeToLiveProperty());;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		long runTime = end-start;
		System.out.println("Execution Time in Milliseconds: "+ runTime);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testGetKeysFromKeystore() {
		
		long start = System.currentTimeMillis();
		
		try {
			System.out.println("The Keys available in the keystore are:\n"+dataStore.getKeysFromKeystore());
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		long runTime = end-start;
		System.out.println("Execution Time in Milliseconds: "+ runTime);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testGetValueFromKeystoreBasedOnKey() {
		long start = System.currentTimeMillis();
		
		System.out.println(dataStore.getValueFromKeystoreBasedOnKey("1000"));
		
		long end = System.currentTimeMillis();
		long runTime = end-start;
		System.out.println("Execution Time in Milliseconds: "+ runTime);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testCheckIfKeyIsReadable() throws FileNotFoundException, IOException, ParseException {
		long start = System.currentTimeMillis();
		
		System.out.println(dataStore.checkIfKeyIsReadable("123"));
		
		long end = System.currentTimeMillis();
		long runTime = end-start;
		System.out.println("Execution Time in Milliseconds: "+ runTime);
	}	
	
	@SuppressWarnings("static-access")
	@Test 
	public void testValidateSizeOfKeystore() {
		long start = System.currentTimeMillis();
		
		boolean isKeyStoreSizeValid = dataStore.validateSizeOfKeystore(constants.getKeyStorePath(),12.0);
		System.out.println("KeyStore size available:" + isKeyStoreSizeValid);
		
		long end = System.currentTimeMillis();
		long runTime = end-start;
		System.out.println("Execution Time in Milliseconds: "+ runTime);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testGetKeyFromUser() {
		System.out.println(dataStore.getKeyFromUser());
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testValidateKeySize() {
		boolean invalidSize = dataStore.validateKeySize("1212121212121212121212121212121212");
		System.out.println("Invalid Scenario:" + !invalidSize);
		
		boolean invalidKey = dataStore.validateKeySize("");
		System.out.println("Invalid Scenario:" + !invalidKey);
		
		boolean validKey = dataStore.validateKeySize("Valid");
		System.out.println("Invalid Scenario:" + !validKey);
		
		boolean validKeyAlphaNum = dataStore.validateKeySize("Valid1234");
		System.out.println("Invalid Scenario:" + !validKeyAlphaNum);
		
	}

}
