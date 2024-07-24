package me.ayydxn.worldsaver.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleDriveAPI
{
    private static GoogleDriveAPI INSTANCE;

    private static final Logger LOGGER = (Logger) LogManager.getLogger("World Saver - Google Drive API");
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final Drive googleDriveService;

    private GoogleDriveAPI() throws IOException, GeneralSecurityException
    {
        NetHttpTransport netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = GoogleDriveUtils.getCredentials(netHttpTransport, JSON_FACTORY, "/assets/world-saver/keys/credentials.json");

        this.googleDriveService = new Drive.Builder(netHttpTransport, JSON_FACTORY, credential)
                .setApplicationName("World Saver")
                .build();
    }

    public static void initialize()
    {
        if (INSTANCE != null)
        {
            LOGGER.warn("You cannot initialize World Saver's Google Drive API integration more than once!");
            return;
        }

        LOGGER.info("Initializing Google Drive API...");

        try
        {
            INSTANCE = new GoogleDriveAPI();
        }
        catch (IOException | GeneralSecurityException exception)
        {
            exception.printStackTrace();
        }
    }

    public static GoogleDriveAPI getInstance()
    {
        if (INSTANCE == null)
            throw new IllegalStateException("Tried to access an instance of the Google Drive API before one was available!");

        return INSTANCE;
    }

    public Drive getDriveService()
    {
        return this.googleDriveService;
    }
}
