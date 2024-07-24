package me.ayydxn.worldsaver.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.common.base.Preconditions;
import me.ayydxn.worldsaver.utils.WorldSaverConstants;
import net.minecraft.util.Util;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GoogleDriveUtils
{
    public static Credential getCredentials(NetHttpTransport netHttpTransport, JsonFactory jsonFactory, String credentialsJSONFilepath)
    {
        try (InputStream credentialsJSONInputStream = GoogleDriveAPI.class.getResourceAsStream(credentialsJSONFilepath))
        {
            Preconditions.checkArgument(credentialsJSONInputStream != null);

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(credentialsJSONInputStream));

            GoogleAuthorizationCodeFlow authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(netHttpTransport, jsonFactory, clientSecrets, GoogleAPIConstants.GOOGLE_DRIVE_SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new File(WorldSaverConstants.WORLD_SAVER_DIRECTORY + "/google-auth")))
                    .setAccessType("offline")
                    .build();

            return new AuthorizationCodeInstalledApp(authorizationCodeFlow, new LocalServerReceiver.Builder()
                    .setPort(8888)
                    .build(), url -> Util.getOperatingSystem().open(url)).authorize("user");

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return null;
    }
}
