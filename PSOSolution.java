package algoProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PSOSolution {
	public static final long serialVersionUID = 1L;
	public static final int VEHICLE_COUNT = 30;
	public static final int MAX_VELOCITY = 4;
	public static final int MAX_ITERATION = 100;

	private static ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

	public static ArrayList<VisitingStations> stations = new ArrayList<VisitingStations>();
	public static final int STATION_COUNT = 10;
	public static final double TARGET = 86.63;
	public static int X[] = new int[STATION_COUNT];
	public static int Y[] = new int[STATION_COUNT];
	public List<List<Integer>> pathList;
	public List<Integer> finalList;

	public void initializePath()

	{
		for (int i = 0; i < STATION_COUNT; i++) {
			X[i] = new Random().nextInt(30);
			Y[i] = new Random().nextInt(30);
		}

		for (int i = 0; i < STATION_COUNT; i++) {
			VisitingStations station = new VisitingStations();
			station.x(X[i]);
			station.y(Y[i]);
			stations.add(station);
		}
		return;
	}

	public void executePSO() {
		Vehicle vehicle = null;
		int iterationNo = 0;
		boolean done = false;
		pathList = new ArrayList<>();

		initialize();

		while (!done) {
			if (iterationNo < MAX_ITERATION) {

				for (int i = 0; i < VEHICLE_COUNT; i++) {
					List<Integer> sublist = new ArrayList<>();
					vehicle = vehicles.get(i);
					System.out.print("Route: ");
					for (int j = 0; j < STATION_COUNT; j++) {
						System.out.print(vehicle.getStationData(j) + ", ");
						sublist.add(vehicle.getStationData(j));
					}

					findTotalDistance(i);
					System.out.print("Distance: " + vehicle.getVehicleBest() + "\n");
					if (vehicle.getVehicleBest() <= TARGET) {
						done = true;
					}
					pathList.add(sublist);
				}

				bubbleSort();

				fitnessFunction();

				updatevehicles();

				System.out.println("iterationNo number: " + iterationNo);

				iterationNo++;

			} else {
				done = true;
			}
		}
		return;
	}

	public void initialize() {
		for (int i = 0; i < VEHICLE_COUNT; i++) {
			Vehicle newVehicle = new Vehicle(STATION_COUNT);
			for (int j = 0; j < STATION_COUNT; j++) {
				newVehicle.setStationData(j, j);
			}
			vehicles.add(newVehicle);
			for (int j = 0; j < 10; j++) {
				randomStationArrange(vehicles.indexOf(newVehicle));
			}
			findTotalDistance(vehicles.indexOf(newVehicle));
		}
		return;
	}

	private static void randomStationArrange(final int index) {
		int stationA = new Random().nextInt(STATION_COUNT);
		int stationB = 0;
		boolean done = false;
		while (!done) {
			stationB = new Random().nextInt(STATION_COUNT);
			if (stationB != stationA) {
				done = true;
			}
		}

		int temp = vehicles.get(index).getStationData(stationA);
		vehicles.get(index).setStationData(stationA, vehicles.get(index).getStationData(stationB));
		vehicles.get(index).setStationData(stationB, temp);
		return;
	}

	private static void fitnessFunction() {
		double worstResults = 0;
		double vValue = 0.0;

		// after sorting, worst will be last in list.
		worstResults = vehicles.get(VEHICLE_COUNT - 1).getVehicleBest();

		for (int i = 0; i < VEHICLE_COUNT; i++) {
			vValue = (MAX_VELOCITY * vehicles.get(i).getVehicleBest()) / worstResults; //Fitness Function

			if (vValue > MAX_VELOCITY) {
				vehicles.get(i).setVehicleVelocity(MAX_VELOCITY);
			} else if (vValue < 0.0) {
				vehicles.get(i).setVehicleVelocity(0.0);
			} else {
				vehicles.get(i).setVehicleVelocity(vValue);
			}
		}
		return;
	}

	private static void updatevehicles() {
		for (int i = 1; i < VEHICLE_COUNT; i++) {
			int changes = (int) Math.floor(Math.abs(vehicles.get(i).getVehicleVelocity()));
			System.out.println("Changes for Vehicle " + i + ": " + changes);
			for (int j = 0; j < changes; j++) {
				if (new Random().nextBoolean()) {
					randomStationArrange(i);
				}
				// Push it closer to it's best neighbor.
				copyFromVehicle(i - 1, i);
			}
			//update pbest value
			findTotalDistance(i);
		}

		return;
	}

	public void printBestSolution() {
		finalList = new ArrayList<>();
		System.out.print("Shortest Route: ");
		for (int j = 0; j < STATION_COUNT; j++) {
			System.out.print(vehicles.get(0).getStationData(j) + ", ");
			finalList.add(vehicles.get(0).getStationData(j));
		} 
		System.out.print("Distance: " + vehicles.get(0).getVehicleBest() + "\n");
		return;
	}

	private static void copyFromVehicle(final int source, final int destination) {
		Vehicle best = vehicles.get(source);
		int targetA = new Random().nextInt(STATION_COUNT);
		int targetB = 0;
		int indexA = 0;
		int indexB = 0;
		int tempIndex = 0;

		int i = 0;
		for (; i < STATION_COUNT; i++) {
			if (best.getStationData(i) == targetA) {
				if (i == STATION_COUNT - 1) {
					targetB = best.getStationData(0);
				} else {
					targetB = best.getStationData(i + 1);
				}
				break;
			}
		}

		for (int j = 0; j < STATION_COUNT; j++) {
			if (vehicles.get(destination).getStationData(j) == targetA) {
				indexA = j;
			}
			if (vehicles.get(destination).getStationData(j) == targetB) {
				indexB = j;
			}
		}

		if (indexA == STATION_COUNT - 1) {
			tempIndex = 0;
		} else {
			tempIndex = indexA + 1;
		}

		int temp = vehicles.get(destination).getStationData(tempIndex);
		vehicles.get(destination).setStationData(tempIndex, vehicles.get(destination).getStationData(indexB));
		vehicles.get(destination).setStationData(indexB, temp);

		return;
	}

	private static void findTotalDistance(final int index) {
		Vehicle vehicle = null;
		vehicle = vehicles.get(index);
		vehicle.setVehicleBest(0.0);

		for (int i = 0; i < STATION_COUNT; i++) {
			if (i == STATION_COUNT - 1) {
				vehicle.setVehicleBest(vehicle.getVehicleBest() + distanceFunction(
						vehicle.getStationData(STATION_COUNT - 1), vehicle.getStationData(0))); // Complete
																								// trip.
			} else {
				vehicle.setVehicleBest(vehicle.getVehicleBest()
						+ distanceFunction(vehicle.getStationData(i), vehicle.getStationData(i + 1)));
			}
		}
		return;
	}

	// Fitness Function
	private static double distanceFunction(final int firstStation, final int secondStation) {
		VisitingStations stationA = null;
		VisitingStations stationB = null;
		double a2 = 0;
		double b2 = 0;
		stationA = stations.get(firstStation);
		stationB = stations.get(secondStation);
		a2 = Math.pow(Math.abs(stationA.x() - stationB.x()), 2);
		b2 = Math.pow(Math.abs(stationA.y() - stationB.y()), 2);

		return Math.sqrt(a2 + b2);
	}

	private static void bubbleSort() {
		boolean done = false;
		while (!done) {
			int changes = 0;
			int listSize = vehicles.size();
			for (int i = 0; i < listSize - 1; i++) {
				if (vehicles.get(i).compareTo(vehicles.get(i + 1)) == 1) {
					Vehicle temp = vehicles.get(i);
					vehicles.set(i, vehicles.get(i + 1));
					vehicles.set(i + 1, temp);
					changes++;
				}
			}
			if (changes == 0) {
				done = true;
			}
		}
		
	}

}
