import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:latlong2/latlong.dart';

// ? Important variables
String urlTemplate = dotenv.env["URL_TEMPLATE"] ?? "";
List<String> subDomain = ['a', 'b', 'c'];
// String apiUrl = dotenv.env["API_URL"] ?? "";
  final Uri baseUri = Uri.base;
  final String baseUrl = baseUri.origin;
  String apiUrl = "$baseUrl/trackingservice/api/v3";
double defaultLatitude = double.parse(dotenv.env["DEFAULT_LATITUDE"] ?? "17.428446");
double defaultLongitude = double.parse(dotenv.env["DEFAULT_LONGITUDE"] ?? "78.302284");
LatLng defaultLatLng = LatLng(defaultLatitude, defaultLongitude);
List<String> siteTypes = ['Waste Land', 'Dumping Ground', 'Landfill'];
