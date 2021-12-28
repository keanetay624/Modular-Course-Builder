package ModularCourseBuilder;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Keane Local
 */
public class BlobHelper {
    
    private static String fileExt;
    private static String pathToString;
    
    //getters and setters

    public static String getFileExt() {
        return fileExt;
    }

    public static void setFileExt(String fileExt) {
        BlobHelper.fileExt = fileExt;
    }

    public static String getPathToString() {
        return pathToString;
    }

    public static void setPathToString(String pathToString) {
        BlobHelper.pathToString = pathToString;
    }
    
    //helper methods
    
//    public static void getResourceBlob(String filepath) throws SQLException, FileNotFoundException, IOException {
//        Database.openConnection();
//        Statement st = Database.getSharedConnection().createStatement();
//
//        String query = "select attachment_name, attachment_ext, blob_data from "
//                + "attachment where resource_id ='"
//                + ResourceHelper.getResourceId() + "';";
//        ResultSet rs = st.executeQuery(query);
//
//        //set file outputstream
//        String downloadFileName = rs.getString(1);
//        AttachmentHelper.setAttachmentName(rs.getString(1));
//        AttachmentHelper.setAttachmentExt(rs.getString(2));
//        File theFile = new File(filepath, downloadFileName);
//        FileOutputStream output = new FileOutputStream(theFile);
//
//        //read the input and store in output
//        if (rs.next()) {
//            InputStream input = rs.getBinaryStream(3);
//
//            byte[] buffer = new byte[4096];
//
//            int len = 0;
//
//            while ((len = input.read(buffer)) != - 1) {
//                output.write(buffer,0,len);
//            }
//
//            System.out.println("File downloaded succcesfully!");
//
//            if (input != null) {
//                input.close();
//            }
//
//            if (output != null) {
//                output.close();
//            }
//
//            Database.getSharedConnection().close();
//            Database.closeConnection();
//
//        }
//    }
//    
//    public static void getSectionBlob(String filepath) throws SQLException, FileNotFoundException, IOException {
//        Database.openConnection();
//        Statement st = Database.getSharedConnection().createStatement();
//
//        String query = "select attachment_name, attachment_ext, blob_data from "
//                + "attachment where section_id ='"
//                + SectionHelper.getSectionId() + "';";
//        ResultSet rs = st.executeQuery(query);
//
//        //set file outputstream
//        String downloadFileName = rs.getString(1);
//        AttachmentHelper.setAttachmentName(rs.getString(1));
//        AttachmentHelper.setAttachmentExt(rs.getString(2));
//        File theFile = new File(filepath, downloadFileName);
//        FileOutputStream output = new FileOutputStream(theFile);
//
//        //read the input and store in output
//        if (rs.next()) {
//            InputStream input = rs.getBinaryStream(3);
//
//            byte[] buffer = new byte[4096];
//
//            int len = 0;
//
//            while ((len = input.read(buffer)) != - 1) {
//                output.write(buffer,0,len);
//            }
//
//            System.out.println("File downloaded succcesfully!");
//
//            if (input != null) {
//                input.close();
//            }
//
//            if (output != null) {
//                output.close();
//            }
//
//            Database.getSharedConnection().close();
//            Database.closeConnection();
//
//        }
//    }
//    
//    public static void getModuleBlob(String filepath) throws SQLException, FileNotFoundException, IOException {
//        Database.openConnection();
//        Statement st = Database.getSharedConnection().createStatement();
//
//        String query = "select attachment_name, attachment_ext, blob_data from "
//                + "attachment where module_id ='"
//                + ModuleHelper.getModuleId() + "';";
//        ResultSet rs = st.executeQuery(query);
//
//        //set file outputstream
//        String downloadFileName = rs.getString(1);
//        AttachmentHelper.setAttachmentName(rs.getString(1));
//        AttachmentHelper.setAttachmentExt(rs.getString(2));
//        File theFile = new File(filepath, downloadFileName);
//        FileOutputStream output = new FileOutputStream(theFile);
//
//        //read the input and store in output
//        if (rs.next()) {
//            InputStream input = rs.getBinaryStream(3);
//
//            byte[] buffer = new byte[4096];
//
//            int len = 0;
//
//            while ((len = input.read(buffer)) != - 1) {
//                output.write(buffer,0,len);
//            }
//
//            System.out.println("File downloaded succcesfully!");
//
//            if (input != null) {
//                input.close();
//            }
//
//            if (output != null) {
//                output.close();
//            }
//
//            Database.getSharedConnection().close();
//            Database.closeConnection();
//
//        }
//    }
//    
//    public static void getCourseBlob(String filepath) throws SQLException, FileNotFoundException, IOException {
//        Database.openConnection();
//        Statement st = Database.getSharedConnection().createStatement();
//
//        String query = "select attachment_name, attachment_ext, blob_data from "
//                + "attachment where course_id ='"
//                + CourseHelper.getCourseId() + "';";
//        ResultSet rs = st.executeQuery(query);
//
//        //set file outputstream
//        String downloadFileName = rs.getString(1);
//        AttachmentHelper.setAttachmentName(rs.getString(1));
//        AttachmentHelper.setAttachmentExt(rs.getString(2));
//        File theFile = new File(filepath, downloadFileName);
//        FileOutputStream output = new FileOutputStream(theFile);
//
//        //read the input and store in output
//        if (rs.next()) {
//            InputStream input = rs.getBinaryStream(3);
//
//            byte[] buffer = new byte[4096];
//
//            int len = 0;
//
//            while ((len = input.read(buffer)) != - 1) {
//                output.write(buffer,0,len);
//            }
//
//            System.out.println("File downloaded succcesfully!");
//
//            if (input != null) {
//                input.close();
//            }
//
//            if (output != null) {
//                output.close();
//            }
//
//            Database.getSharedConnection().close();
//            Database.closeConnection();
//
//        }
//    }
    
//    public static String getFileType() throws SQLException {
//        Database.openConnection();
//        Statement st = Database.getSharedConnection().createStatement();
//        
//        String query = "select attachment_ext from attachment where resource_id ='"
//                + ResourceHelper.getResourceId() + "';";
//        ResultSet rs = st.executeQuery(query);
//        setFileExt(rs.getString(1));
//        
//        Database.getSharedConnection().close();
//        Database.closeConnection();
//        return fileExt;
//    }
//
//    public static String getBlobtoTempFile(String tempPath) throws SQLException, FileNotFoundException, IOException {
//        Database.openConnection();
//        Statement st = Database.getSharedConnection().createStatement();
//
//        String query = "select blob_data, attachment_ext, attachment_name from attachment where resource_id ='"
//                + ResourceHelper.getResourceId() + "';";
//        ResultSet rs = st.executeQuery(query);
//
//        //set file outputstream
//        File theFile = new File(tempPath);
//        FileOutputStream output = new FileOutputStream(theFile);
//        System.out.println("extension: " + fileExt);
//        //read the input and store in output
//        if (rs.next()) {
//            InputStream input = rs.getBinaryStream(1);
//
//            byte[] buffer = new byte[4096];
//
//            int len = 0;
//
//            while ((len = input.read(buffer)) != - 1) {
//                output.write(buffer,0,len);
//            }
//
//            System.out.println("File downloaded succcesfully!");
//
//            if (input != null) {
//                input.close();
//            }
//
//            if (output != null) {
//                output.close();
//            }
//
//            Database.getSharedConnection().close();
//            Database.closeConnection();
//
//        }
//            return theFile.getAbsolutePath();
//    }

}
