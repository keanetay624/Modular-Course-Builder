/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModularCourseBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import javafx.stage.FileChooser;

/**
 *
 * @author Keane Local
 */
public class FileHelper {
    private static final int COURSE_TYPE = 1;
    private static final int MODULE_TYPE = 2;
    private static final int SECTION_TYPE = 3;
    private static final int RESOURCE_TYPE = 4;
    
    private String name;
    private String ext;
    
    public void getFile(int modelType, String idString, int idInt) throws SQLException, IOException {
        String filePath = "";
        FileChooser fc = new FileChooser();
        
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Images","*.*"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF","*.pdf"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT","*.txt"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PPTX","*.pptx"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("DOCX","*.docx"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLSX","*.xlsx"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV","*.csv"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP4","*.mp4"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3","*.mp3"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("ZIP","*.zip"));
        
        File f = fc.showOpenDialog(null);
        
        // save filepath and extension
        if (f != null) {
            filePath = f.getAbsolutePath();
            String fileName = f.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, f.getName().length());
            name = fileName;
            ext = fileExtension;
            System.out.println(filePath);
            System.out.println("Extension: " + fileExtension);
            BlobHelper.setFileExt(fileExtension);
        }
        
        // convert file at path to a blob type
        byte[] tempByteArray = convertFileContentToBlob(filePath);
        
        if (modelType == 1) {
            DatabaseHelper.insertCourseAttachment(tempByteArray, name, idString);
        } else if (modelType == 2) {
            DatabaseHelper.insertModuleAttachment(tempByteArray, name, idString);
        } else if (modelType == 3) {
            DatabaseHelper.insertSectionAttachment(tempByteArray, name, idInt);
        } else {
            DatabaseHelper.insertResourceAttachment(tempByteArray, name, idInt);
        }
        
    }
    
    public static byte[] convertFileContentToBlob(String filepath) throws IOException {
        //create file object
        File userFile = new File(filepath);

        //initialize a byte array of size of the file
        byte[] fileContent = new byte[(int) userFile.length()];
        FileInputStream inputStream = null;

        try {
            //create an input stream pointing to the file
            inputStream = new FileInputStream(userFile);
            //read the contents of file into byte Array
            inputStream.read(fileContent);
        } catch (IOException e) {
            throw new IOException ("Unable to convert file to byte array. " +
                    e.getMessage());
        } finally {
            //close input stream
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return fileContent;
    }
}
