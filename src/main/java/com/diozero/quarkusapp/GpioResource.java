package com.diozero.quarkusapp;

import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.diozero.internal.spi.NativeDeviceFactoryInterface;
import com.diozero.sbc.DeviceFactoryHelper;

@Path("/gpios")
public class GpioResource {
	private NativeDeviceFactoryInterface deviceFactory;

	private synchronized void init() {
		if (deviceFactory == null) {
			deviceFactory = DeviceFactoryHelper.getNativeDeviceFactory();
		}
	}

	@GET
	public Set<Gpio> list() {
		init();

		return deviceFactory.getBoardPinInfo().getGpioPins().stream()
				.map(pin -> new Gpio(pin.getDeviceNumber(),
						deviceFactory.getGpioValue(pin.getDeviceNumber()) == 0 ? false : true))
				.collect(Collectors.toSet());
	}
}
