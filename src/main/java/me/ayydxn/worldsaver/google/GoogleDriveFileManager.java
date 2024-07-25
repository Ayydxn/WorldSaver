package me.ayydxn.worldsaver.google;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.common.collect.Lists;

import java.io.IOException;

public class GoogleDriveFileManager
{
    private final Drive driveService;

    public GoogleDriveFileManager(Drive driveService)
    {
        this.driveService = driveService;
    }

    public void uploadFile(String folderID, String mimeType, java.io.File fileOnDisk) throws IOException
    {
        /*------------------------------------------------------------------------*/
        /* -- Check if the file already exists. If it does, we return its ID. -- */
        /*-----------------------------------------------------------------------*/

        boolean doesFileAlreadyExist = false;

        FileList fileList = this.driveService.files().list()
                .setQ("'" + folderID + "' in parents and mimeType='" + mimeType + "' and trashed=false")
                .execute();

        for (File file : fileList.getFiles())
        {
            if (file.getName().equals(fileOnDisk.getName()))
            {
                GoogleDriveAPI.getLogger().info("File '{}' already exists...", fileOnDisk.getName());

                doesFileAlreadyExist = true;

                break;
            }
        }

        if (doesFileAlreadyExist)
            return;

        File fileMetadata = new File();
        fileMetadata.setName(fileOnDisk.getName());
        fileMetadata.setParents(Lists.newArrayList(folderID));

        FileContent fileContent = new FileContent(mimeType, fileOnDisk);

        this.driveService.files().create(fileMetadata, fileContent)
                .setFields("id, parents")
                .execute();

        GoogleDriveAPI.getLogger().info("Uploaded file '{}'...", fileOnDisk.getName());
    }

    public String createFolder(String folderName) throws IOException
    {
        /*-------------------------------------------------------------------------*/
        /* -- Check if the folder already exists. If it does, we return its ID. -- */
        /*-------------------------------------------------------------------------*/

        FileList fileList = this.driveService.files().list()
                .setQ("mimeType='application/vnd.google-apps.folder'")
                .execute();

        for (File file : fileList.getFiles())
        {
            if (file.getName().equals(folderName))
            {
                GoogleDriveAPI.getLogger().info("Folder '{}' already exists...", folderName);

                return file.getId();
            }
        }

        /*----------------------------------------------------------------------*/
        /* -- The folder doesn't already exist. We create it and return its ID. -- */
        /*----------------------------------------------------------------------*/

        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");

        File createdFolder = this.driveService.files().create(folderMetadata)
                .setFields("id")
                .execute();

        GoogleDriveAPI.getLogger().info("Created Folder '{}'", folderName);

        return createdFolder.getId();
    }


    public String createNestedFolder(String parentFolderID, String childFolderName) throws IOException
    {
        /*-------------------------------------------------------------------------------*/
        /* -- Check if the child folder already exists. If it does, we return its ID. -- */
        /*-------------------------------------------------------------------------------*/

        FileList fileList = this.driveService.files().list()
                .setQ("'" + parentFolderID + "' in parents and mimeType='application/vnd.google-apps.folder' and trashed=false")
                .execute();

        for (File file : fileList.getFiles())
        {
            if (file.getName().equals(childFolderName))
            {
                GoogleDriveAPI.getLogger().info("Nested folder '{}' already exists...", childFolderName);

                return file.getId();
            }
        }

        /*----------------------------------------------------------------------*/
        /* -- The child folder doesn't already exist. We create it and return its ID. -- */
        /*----------------------------------------------------------------------*/

        File childFolderMetadata = new File();
        childFolderMetadata.setName(childFolderName);
        childFolderMetadata.setMimeType("application/vnd.google-apps.folder");
        childFolderMetadata.setParents(Lists.newArrayList(parentFolderID));

        File createdChildFolder = this.driveService.files().create(childFolderMetadata)
                .setFields("id, parents")
                .execute();

        return createdChildFolder.getId();
    }
}
