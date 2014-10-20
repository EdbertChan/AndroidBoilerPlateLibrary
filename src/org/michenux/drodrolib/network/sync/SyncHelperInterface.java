package org.michenux.drodrolib.network.sync;

public interface SyncHelperInterface{
	public boolean isSyncing();
	public void setSyncState(boolean newState);
	
	public boolean equals(SyncHelperInterface e);
	
	
}