package me.ayydxn.worldsaver.google;

import com.google.api.services.drive.DriveScopes;
import com.google.common.collect.Lists;

import java.util.List;

public class GoogleAPIConstants
{
    public static final List<String> GOOGLE_DRIVE_SCOPES = Lists.newArrayList(DriveScopes.DRIVE);
}
