package union.union_vr1.Vistas;

import android.preference.PreferenceActivity;
import android.os.Bundle;

import union.union_vr1.R;

public class AppPreferences extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
