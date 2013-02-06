import ch.aplu.jgamegrid.GGVector;

public enum StrokeDirection {
		North(new GGVector(0,1)), East(new GGVector(1,0)), South(new GGVector(0,-1)), West(new GGVector(-1,0));
		
		private GGVector offset;
		
		StrokeDirection(GGVector offset) {
			this.offset = offset;
		}
		
		public GGVector getOffset() {
			return offset;
		}
}
