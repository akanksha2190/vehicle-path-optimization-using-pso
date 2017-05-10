package algoProject;



public class Vehicle {
	 private int stationData[];
	    private double vehicleBest = 0;
	    private double vehicleVelocity = 0.0;
	
	    public int getStationData(int position) {
			return this.stationData[position];
		}

		public void setStationData(int position,int value) {
			this.stationData[position]=value;
		}

		public double getVehicleBest() {
			return vehicleBest;
		}

		public void setVehicleBest(double vehicleBest) {
			this.vehicleBest = vehicleBest;
		}

		public double getVehicleVelocity() {
			return vehicleVelocity;
		}

		public void setVehicleVelocity(double vehicleVelocity) {
			this.vehicleVelocity = vehicleVelocity;
		}

		public Vehicle(int stations)
	    {	stationData=new int[stations];
	        this.vehicleBest = 0;
	        this.vehicleVelocity = 0.0;
	    }
	    
	    public int compareTo(Vehicle vehicle)
	    {
	    	if(this.getVehicleBest() < vehicle.getVehicleBest()){
	    		return -1;
	    	}else if(this.getVehicleBest() > vehicle.getVehicleBest()){
	    		return 1;
	    	}else{
	    		return 0;
	    	}
	    }
	
	    

}
