package org.nzhegalin.estimate.entity;

import java.io.Serializable;

public class ManhourResource extends Resource implements Serializable {

	private static final long serialVersionUID = 1L;

	public ManhourResource() {
		type = 'h';
	}

}
