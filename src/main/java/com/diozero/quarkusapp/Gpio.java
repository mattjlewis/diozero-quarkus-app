package com.diozero.quarkusapp;

public class Gpio {
	public int number;
	public boolean state;

	public Gpio() {
	}

	public Gpio(int number, boolean state) {
		this.number = number;
		this.state = state;
	}
}
