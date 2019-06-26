package arenashooter.game.gameStates.editor.addItem;

import java.util.List;

import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.MultiUi;

public enum ItemType {
	USABLE {
		@Override
		public MultiUi getEditor() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	GUN {
		@Override
		public MultiUi getEditor() {
			return new AddGunEditor();
		}
	},
	MELEE {
		@Override
		public MultiUi getEditor() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	SHOTGUN {
		@Override
		public MultiUi getEditor() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	USABLETIME {
		@Override
		public MultiUi getEditor() {
			// TODO Auto-generated method stub
			return null;
		}

	};
	public abstract MultiUi getEditor();
}
