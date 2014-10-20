package org.michenux.drodrolib.network.sync;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public abstract class AbstractSyncHelper implements SyncHelperInterface {
	protected Account mAccount;
	protected String mAuthority;
protected Context c;
	
	public AbstractSyncHelper(Context c) {
		this.c = c;
		String accountName = getDefaultAccountName();
		String accountType = getDefaultAccountType();
		String authority = getDefaultAuthority();
		createAccount(accountName, accountType, authority, false, c);
		

	}

	public AbstractSyncHelper(boolean enableSync, Context c) {
		this.c = c;
		String accountName = getDefaultAccountName();
		String accountType = getDefaultAccountType();
		String authority = getDefaultAuthority();
		createAccount(accountName, accountType, authority, enableSync, c);
	
	}

	public boolean createAccount(String accountName, String accountType,
			String authority, boolean enableSync, Context context) {

		this.mAccount = new Account(accountName, accountType);
		this.mAuthority = authority;
		AccountManager accountManager = (AccountManager) context
				.getSystemService(Context.ACCOUNT_SERVICE);
		boolean result = accountManager.addAccountExplicitly(mAccount, null,
				null);

		return result;
	}

	public abstract String getDefaultAccountName();

	public abstract String getDefaultAccountType();

	public abstract String getDefaultAuthority();

	public Account getAccount() {
		return mAccount;
	}

	public String getAuthority() {
		return mAuthority;
	}

	public void performSync() {
		Bundle settingsBundle = new Bundle();
		settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
		settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_FORCE, true);
		ContentResolver.requestSync(mAccount, mAuthority, settingsBundle);
	}

	public void performSync(Bundle extras) {
		Bundle settingsBundle = new Bundle();
		settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
		settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_FORCE, true);
		settingsBundle.putAll(extras);
		
		ContentResolver.requestSync(mAccount, mAuthority, settingsBundle);
	}

	protected boolean syncState = false;
	public void setSyncState(boolean newState){
		syncState = newState;
	}
	public boolean isSyncing() {
		return syncState;
	//	return (ContentResolver.isSyncActive(mAccount, mAuthority) && ContentResolver.isSyncPending(mAccount, mAuthority));
	}
}