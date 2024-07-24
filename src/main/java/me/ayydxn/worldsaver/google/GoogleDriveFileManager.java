package me.ayydxn.worldsaver.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;

public class GoogleDriveFileManager
{
    private final Drive driveService;

    public GoogleDriveFileManager(Drive driveService)
    {
        this.driveService = driveService;
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
        /* -- The folder doesn't already exist. We create and return its ID. -- */
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
}
