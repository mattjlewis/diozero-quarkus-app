package com.diozero.quarkusapp;

import java.io.Serializable;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Gpio implements Serializable {
	private static final long serialVersionUID = -266213869530666744L;

	public int number;
	public boolean state;

	public Gpio() {
	}

	public Gpio(int number, boolean state) {
		this.number = number;
		this.state = state;
	}

	public int number() {
		return number;
	}

	public boolean state() {
		return state;
	}
}
