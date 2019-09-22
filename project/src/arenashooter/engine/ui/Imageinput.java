package arenashooter.engine.ui;

import arenashooter.engine.graphics.TextureI;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.game.Main;

public enum Imageinput {
	UP {
		@Override
		public UiImage getImage() {
			TextureI texture = Main.getRenderer().loadTexture("data/sprites/interface/up.png");
			return new UiImage(texture);
		}
	},
	DOWN {
		@Override
		public UiImage getImage() {
			TextureI texture = Main.getRenderer().loadTexture("data/sprites/interface/down.png");
			return new UiImage(texture);
		}
	},
	LEFT {
		@Override
		public UiImage getImage() {
			TextureI texture = Main.getRenderer().loadTexture("data/sprites/interface/left.png");
			return new UiImage(texture);
		}
	},
	RIGHT {
		@Override
		public UiImage getImage() {
			TextureI texture = Main.getRenderer().loadTexture("data/sprites/interface/right.png");
			return new UiImage(texture);
		}
	},
	SPACE {
		@Override
		public UiImage getImage() {
			TextureI texture = Main.getRenderer().loadTexture("data/sprites/interface/Space.png");
			return new UiImage(texture);
		}
	},
	ESCAPE {
		@Override
		public UiImage getImage() {
			TextureI texture = Main.getRenderer().loadTexture("data/sprites/interface/Escape.png");
			return new UiImage(texture);
		}
	},
	A {
		@Override
		public UiImage getImage() {
			TextureI texture = Main.getRenderer().loadTexture("data/sprites/interface/Press_A.png");
			return new UiImage(texture);
		}
	},
	B {
		@Override
		public UiImage getImage() {
			TextureI texture = Main.getRenderer().loadTexture("data/sprites/interface/Press_B.png");
			return new UiImage(texture);
		}
	}
//		,START {
//			@Override
//			public UiImage getImage() {
//				Texture texture = Texture.loadTexture("data/sprites/interface/Space.png");
//				return new UiImage(texture);
//			}
//		}
	;

	public abstract UiImage getImage();
}