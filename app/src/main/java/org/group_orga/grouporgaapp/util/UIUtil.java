package org.group_orga.grouporgaapp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;

import org.group_orga.grouporgaapp.R;
import org.group_orga.grouporgaapp.api.OrgaAPIAccessor;

import java8.util.function.Function;


public class UIUtil {
    public static Function<Throwable, Void> defaultAPIErrorHandler(Activity context) {
        return throwable -> {
            context.runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.api_error_title);
                if (throwable instanceof OrgaAPIAccessor.APIException) {
                    OrgaAPIAccessor.APIException apiException = (OrgaAPIAccessor.APIException) throwable;
                    String message = context.getString(R.string.api_error_message, apiException.getResponse().code(), apiException.getResponse().errorBody());
                    builder.setMessage(message);
                } else {
                    builder.setMessage(throwable.getMessage());
                }

                Log.e("API", "API call failed", throwable);
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                });
                builder.create().show();
            });
            return null;
        };
    }
}
