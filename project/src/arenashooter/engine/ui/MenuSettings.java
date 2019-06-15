package arenashooter.engine.ui;

import java.text.DecimalFormat;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.game.Main;

public class MenuSettings extends TabList<UiActionable> {

	private DecimalFormat df = new DecimalFormat("0.0");
	private UiActionable back = new UiActionable() {

		@Override
		public void draw() {
			// Nothing
		}
	};

	public MenuSettings(float y, Trigger onBack) {
		back.setOnArm(onBack);

		UiListVertical<UiActionable> audio = new UiListVertical<>(), video = new UiListVertical<>();
		addBind("Video", video);
		addBind("Audio", audio);
		setPosition(0, y);
		addToScale(-4);
		audio.setSpacing(7.5);
		video.setSpacing(7.5);

		///
		/// Audio settings
		///

		ScrollerH<Float> mainVolume = new ScrollerH<>();
		for (int i = 0; i <= 10; i++) {
			Float f = Float.valueOf((float) (i * 0.1));
			mainVolume.add(f);
			mainVolume.changeValueView(f, df.format(f.floatValue()));
		}
		mainVolume.setTitle("Main volume");
		if (!mainVolume.setValue(Audio.getMainVolume()))
			Main.log.warn("Couldn't set main volume button initial value");
		mainVolume.setOnValidation(new Trigger() {

			@Override
			public void make() {
				Audio.setMainVolume(mainVolume.get().floatValue());
			}
		});
		ScrollerH<Float> musicVolume = new ScrollerH<>();
		for (int i = 0; i <= 10; i++) {
			Float f = Float.valueOf((float) (i * 0.1));
			musicVolume.add(f);
			musicVolume.changeValueView(f, df.format(f.floatValue()));
		}
		musicVolume.setTitle("Music volume");
		if (!musicVolume.setValue(Audio.getChannelVolume(AudioChannel.MUSIC)))
			Main.log.warn("Couldn't set music volume button initial value");
		musicVolume.setOnValidation(new Trigger() {

			@Override
			public void make() {
				Audio.setChannelVolume(AudioChannel.MUSIC, musicVolume.get().floatValue());
			}
		});

		ScrollerH<Float> sfxVolume = new ScrollerH<>();
		for (int i = 0; i <= 10; i++) {
			Float f = Float.valueOf((float) (i * 0.1));
			sfxVolume.add(f);
			sfxVolume.changeValueView(f, df.format(f.floatValue()));
		}
		sfxVolume.setTitle("SFX volume");
		if (!sfxVolume.setValue(Audio.getChannelVolume(AudioChannel.SFX)))
			Main.log.warn("Couldn't set sfx volume button initial value");
		sfxVolume.setOnValidation(new Trigger() {

			@Override
			public void make() {
				Audio.setChannelVolume(AudioChannel.SFX, sfxVolume.get().floatValue());
			}
		});

		ScrollerH<Float> uiVolume = new ScrollerH<>();
		for (int i = 0; i <= 10; i++) {
			Float f = Float.valueOf((float) (i * 0.1));
			uiVolume.add(f);
			uiVolume.changeValueView(f, df.format(f.floatValue()));
		}
		uiVolume.setTitle("UI volume");
		if (!uiVolume.setValue(Audio.getChannelVolume(AudioChannel.UI)))
			Main.log.warn("Couldn't set ui volume button initial value");

		uiVolume.setOnValidation(new Trigger() {

			@Override
			public void make() {
				Audio.setChannelVolume(AudioChannel.UI, uiVolume.get().floatValue());
			}
		});

		///
		/// Video settings
		///

		ScrollerH<int[]> reso = new ScrollerH<>();
		for (int[] is : Window.getAvailableResolutions()) {
			reso.add(is);
			reso.changeValueView(is, is[0] + "x" + is[1]);
			if (is[0] == Window.getWidth() && is[1] == Window.getHeight()) {
				if (!reso.setValue(is))
					Main.log.warn("Couldn't set resolution button initial value");
			}
		}
		reso.setTitle("Resolution");
		reso.setOnValidation(new Trigger() {

			@Override
			public void make() {
				Window.resize(reso.get()[0], reso.get()[1]);
			}
		});

		ScrollerH<Boolean> fullscreen = new ScrollerH<>();
		fullscreen.add(false);
		fullscreen.changeValueView(false, "windowed");
		fullscreen.add(true);
		fullscreen.changeValueView(true, "fullscreen");
		fullscreen.setTitle("Mode");
		if (!fullscreen.setValue(Window.isFullscreen()))
			Main.log.warn("Couldn't set fullscreen button initial value");
		fullscreen.setOnValidation(new Trigger() {
			@Override
			public void make() {
				Window.setFullscreen(fullscreen.get().booleanValue());
			}
		});

		ScrollerH<Float> scale = new ScrollerH<>();
		for (int i = 5; i <= 40; i++) {
			Float f = Float.valueOf((float) (i * 0.05));
			scale.add(f);
			scale.changeValueView(f, df.format(f.floatValue()));
		}
		if (scale.containValue(Window.getResScale())) {
			if (!scale.setValue(Window.getResScale())) {
				Main.log.warn("Couldn't set resolution scale button initial value");
			}
		} else {
			Main.log.warn(
					"Couldn't set resolution scale button initial value because is's not on values added in button");
		}

		scale.setTitle("Resolution scale");
		scale.setOnValidation(new Trigger() {
			@Override
			public void make() {
				Window.setResScale(scale.get().floatValue());
			}
		});

		Button backVideo = new Button("Back");
		backVideo.setOnArm(onBack);
		backVideo.setRectangleVisible(false);
		Button backAudio = new Button("Back");
		backAudio.setOnArm(onBack);
		backAudio.setRectangleVisible(false);

		video.addElements(reso, fullscreen, scale, backVideo);
		audio.addElements(mainVolume, musicVolume, sfxVolume, uiVolume, backAudio);

		backVideo.addToPosition(0, 2.5);
		backAudio.addToPosition(0, 2.5);

	}

	/* Back */
	@Override
	public boolean backAction() {
		if (!getTarget().backAction()) {
			back.arm();
		}
		return true;
	}

}