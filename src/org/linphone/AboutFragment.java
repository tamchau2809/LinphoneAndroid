package org.linphone;

import org.linphone.core.LinphoneCore;
import org.linphone.mediastream.Log;

import android.app.Fragment;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Sylvain Berfini
 */
public class AboutFragment extends Fragment implements OnClickListener {
	View sendLogButton = null;
	View resetLogButton = null;
	ImageView cancel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.about, container, false);

		TextView aboutVersion = (TextView) view.findViewById(R.id.about_android_version);
		TextView aboutLiblinphoneVersion = (TextView) view.findViewById(R.id.about_liblinphone_version);
		aboutLiblinphoneVersion.setText(String.format(getString(R.string.about_liblinphone_version), LinphoneManager.getLc().getVersion()));
		try {
			aboutVersion.setText(String.format(getString(R.string.about_version), getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName));
		} catch (NameNotFoundException e) {
			Log.e(e, "cannot get version name");
		}

		cancel = (ImageView) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(this);

		sendLogButton = view.findViewById(R.id.send_log);
		sendLogButton.setOnClickListener(this);
		sendLogButton.setVisibility(org.linphone.LinphonePreferences.instance().isDebugEnabled() ? View.VISIBLE : View.GONE);

		resetLogButton = view.findViewById(R.id.reset_log);
		resetLogButton.setOnClickListener(this);
		resetLogButton.setVisibility(org.linphone.LinphonePreferences.instance().isDebugEnabled() ? View.VISIBLE : View.GONE);

		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (org.linphone.LinphoneActivity.isInstanciated()) {
			LinphoneActivity.instance().selectMenu(FragmentsAvailable.ABOUT);
		}
	}

	@Override
	public void onClick(View v) {
		if (org.linphone.LinphoneActivity.isInstanciated()) {
			LinphoneCore lc = org.linphone.LinphoneManager.getLcIfManagerNotDestroyedOrNull();
			if (v == sendLogButton) {
				if (lc != null) {
					lc.uploadLogCollection();
				}
			} else if (v == resetLogButton) {
				if (lc != null) {
					lc.resetLogCollection();
				}
			} else if (v == cancel) {
				LinphoneActivity.instance().displayDialer();
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	
}
