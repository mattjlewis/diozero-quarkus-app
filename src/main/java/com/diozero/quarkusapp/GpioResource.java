package com.diozero.quarkusapp;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.diozero.api.DigitalOutputDevice;
import com.diozero.internal.spi.NativeDeviceFactoryInterface;
import com.diozero.sbc.DeviceFactoryHelper;
import com.diozero.util.SleepUtil;

@Path("gpios")
@SuppressWarnings("resource")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class GpioResource {
	private NativeDeviceFactoryInterface deviceFactory;
	private Map<Integer, DigitalOutputDevice> gpios = new HashMap<>();

	public GpioResource() {
		init();
	}

	private synchronized void init() {
		System.out.println("** init **");

		if (deviceFactory == null) {
			deviceFactory = DeviceFactoryHelper.getNativeDeviceFactory();
		}

		// Validate that the diozero-system-utils library has been loaded correctly
		SleepUtil.sleepNanos(0, 100);
	}

	@GET
	public Response list() {
		return Response.ok(deviceFactory.getBoardPinInfo().getGpioPins().stream()
				.map(pin -> new Gpio(pin.getDeviceNumber(),
						deviceFactory.getGpioValue(pin.getDeviceNumber()) == 0 ? false : true))
				.sorted((Comparator.comparingInt(Gpio::number)))).build();
	}

	@Path("{gpio}")
	public Response value(@PathParam("gpio") int gpio) {
		return Response.ok(Boolean.valueOf(deviceFactory.getGpioValue(gpio) == 0 ? false : true)).build();
	}

	@Path("{gpio}/on")
	public Response on(@PathParam("gpio") int gpio) {
		gpios.computeIfAbsent(Integer.valueOf(gpio), i -> new DigitalOutputDevice(i.intValue())).on();
		return Response.ok().build();
	}

	@Path("{gpio}/off")
	public Response off(@PathParam("gpio") int gpio) {
		gpios.computeIfAbsent(Integer.valueOf(gpio), i -> new DigitalOutputDevice(i.intValue())).off();
		return Response.ok().build();
	}

	@Path("{gpio}/toggle")
	public Response toggle(@PathParam("gpio") int gpio) {
		gpios.computeIfAbsent(Integer.valueOf(gpio), i -> new DigitalOutputDevice(gpio)).toggle();
		return Response.ok().build();
	}
}
