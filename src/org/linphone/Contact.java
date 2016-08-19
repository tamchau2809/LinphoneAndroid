package org.linphone;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.linphone.compatibility.Compatibility;
import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneFriend;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * @author Sylvain Berfini
 * @deprecated
 */
public class Contact implements Serializable {
	private static final long serialVersionUID = 3790717505065723499L;
	
	private String id;
	private String name;
	private transient Uri photoUri;
	private transient Uri thumbnailUri;
	private transient Bitmap photo;
	private List<String> numbersOrAddresses;
	private boolean hasFriends;
	private LinphoneAddress address;
	
	public Contact(String id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.photoUri = null;
		this.thumbnailUri = null;
		this.hasFriends = false;
		this.address = null;
	}

	public Contact(String id, LinphoneAddress address) {
		super();
		this.id = id;
		this.name = LinphoneUtils.getAddressDisplayName(address);
		this.photoUri = null;
		this.thumbnailUri = null;
		this.address = address;

	}

	public Contact(String id, String name, Uri photo, Uri thumbnail) {
		super();
		this.id = id;
		this.name = name;
		this.photoUri = photo;
		this.thumbnailUri = thumbnail;
		this.photo = null;
		this.hasFriends = false;
		this.address = null;
	}
	
	public Contact(String id, String name, Uri photo, Uri thumbnail, Bitmap picture) {
		super();
		this.id = id;
		this.name = name;
		this.photoUri = photo;
		this.thumbnailUri = thumbnail;
		this.photo = picture;
		this.hasFriends = false;
		this.address = null;
	}


	public boolean hasFriends() {
		return hasFriends;
	}
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public LinphoneAddress getLinphoneAddress() {
		return address;
	}

	public Uri getPhotoUri() {
		return photoUri;
	}

	public Uri getThumbnailUri() {
		return thumbnailUri;
	}
	
	public Bitmap getPhoto() {
		return photo;
	}

	public List<String> getNumbersOrAddresses() {
		if (numbersOrAddresses == null)
			numbersOrAddresses = new ArrayList<String>();
		return numbersOrAddresses;
	}
	
	public void refresh(ContentResolver cr) {
		this.numbersOrAddresses = Compatibility.extractContactNumbersAndAddresses(id, cr);
		LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
		if(lc != null && lc.getFriendList() != null) {
			for (LinphoneFriend friend :lc.getFriendList()){
				if (id.equals(friend.getRefKey())) {
					hasFriends = true;
					this.numbersOrAddresses.add(friend.getAddress().asStringUriOnly());
				}
			}
		}
		this.name = Compatibility.refreshContactName(cr, id);
	}
}
