package com.acv.cheerz.service;

import android.os.Binder;

public class CheerzBin extends Binder {
	private CheerzService cheerzService;

	public CheerzBin(CheerzService cheerzService) {
		super();
		this.cheerzService = cheerzService;
	}

	public CheerzService getCheerzService() {
		return cheerzService;
	}
}
