package arenashooter.engine.ui;

import java.text.DecimalFormat;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;

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

		UiListVertical<UiActionable> audio = new UiListVertical<>(),  video = new UiListVertical<>();
		audio.setSpacing(8);
		video.setSpacing(8);
		
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
		}
		reso.setTitle("Resolution");
		reso.setOnValidation(new Trigger() {

			@Override
			public void make() {
				Window.resize(reso.get()[0], reso.get()[1]);
			}
		});

		ScrollerH<Float> scale = new ScrollerH<>();
		for (int i = 5; i <= 20; i++) {
			Float f = Float.valueOf((float) (i * 0.1));
			scale.add(f);
			scale.changeValueView(f, df.format(f.floatValue()));
		}
		scale.setTitle("Scale");
		scale.setOnValidation(new Trigger() {

			@Override
			public void make() {
				Window.setResScale(scale.get().floatValue());
			}
		});

		Button backAudio = new Button("Back");
		backAudio.setOnArm(onBack);
		backAudio.setRectangleVisible(false);
		Button backVideo = new Button("Back");
		backVideo.setOnArm(onBack);
		backVideo.setRectangleVisible(false);
		audio.addElements(mainVolume, musicVolume, sfxVolume, uiVolume, backAudio);
		video.addElements(reso, scale, backVideo);
		
		backAudio.addToPosition(0, 2.5);
		backVideo.addToPosition(0, 2.5);

		addBind("Audio", audio);
		addBind("Video", video);

		setPosition(0, y);
		addToScale(-4);
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