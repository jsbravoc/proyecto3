package model.data_structures;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.vo.InfoServicios;
import model.vo.Servicio;

public class Graph <K extends Comparable<K>, V, A>{

	@JsonProperty
	private int arcos;
	@JsonProperty 
	private hashTableSeparateChaining<K, Vertex> adj;
	@JsonProperty 
	private Lista<Edge> listaArcos;


	//----------------------------------------------------------------------------------
	//Clases Auxiliares
	//----------------------------------------------------------------------------------
	public class Edge implements Comparable<Edge>{

		//private K fuente;

		@JsonProperty 
		private Vertex fuente;

		@JsonProperty 
		private Vertex destino;

		//private K destino;
		@JsonProperty 
		private A infoArco;

		public Edge(K pFuente, V pFuenteValue, K pDestino, V pDestinoValue, A pInfoArco)
		{

			fuente = null;
			if (fuente == null)
			{
				fuente = new Vertex(pFuente,pFuenteValue);

			}
			//adj.put(pFuente, fuente);
			destino = null;
			if (destino == null)
			{
				destino = new Vertex (pDestino, pDestinoValue);

			}
			//adj.put(pDestino, destino);
			infoArco = pInfoArco;
			fuente.agregarOutcoming(destino);
			destino.agregarIncoming(fuente);
		}

		public Edge(K pFuente, K pDestino, A pInfoArco)
		{
			//TODO: Revisar necesidad agregar adj
			fuente = findVertex(pFuente);
			destino = findVertex(pDestino);
			infoArco = pInfoArco;
			fuente.agregarOutcoming(destino);
			destino.agregarIncoming(fuente);
		}
		public Vertex getFuente()	
		{		
			return fuente;	
		}

		public Vertex getDestino()	
		{		
			return destino;	
		}

		public A getInfo()	
		{		
			return infoArco;	
		}

		public void setInfoArc(A pInforArc)
		{
			infoArco = pInforArc;
		}

		@Override
		public int compareTo(Edge pEdge) 
		{
			return fuente.equals(pEdge.getFuente()) && destino.equals(pEdge.getDestino())? 0: -1;
		}
	}

	public class Vertex implements Comparable <Vertex> 
	{
		@JsonProperty 
		private K idVertex;
		@JsonProperty 
		private V informacion;
		@JsonProperty 
		private Lista<Edge> arcos;

		//Vertices adj que llegan a este vertice
		@JsonProperty 
		private Lista<Vertex> incoming;
		//Vertices adj de los que sale este vertice
		@JsonProperty
		private Lista<Vertex> outcoming;

		@JsonProperty
		private boolean marcado;

		public Vertex(K ident, V pInformacion)
		{
			idVertex = ident;
			informacion = pInformacion;
			marcado = false;
			incoming = new Lista<Vertex>();
			outcoming = new Lista<Vertex>();
			arcos = new Lista<Edge>();
		}

		public K id() 
		{
			return idVertex;
		}
		public V info()
		{ 
			return informacion;
		}
		public Lista<Edge> arcos()
		{
			return arcos;
		}
		public boolean marcado()
		{			
			return marcado;		
		}
		public int compareTo(Vertex v)
		{
			return idVertex.compareTo(v.id());
		}
		public void agregarIncoming (Vertex v)
		{
			incoming.add(v);
		}
		public void agregarOutcoming (Vertex v)
		{
			outcoming.add(v);
		}

		public void setInfoVer(V pInformacion)
		{
			informacion = pInformacion;
		}


	}

	//---------------------------------------------------------
	//Constructor
	//---------------------------------------------------------

	public Graph(int V) 
	{
		adj = new hashTableSeparateChaining<>(V);
		arcos=0;
		listaArcos = new Lista<Edge>();
	}

	//Numeros de vertices
	public int V()
	{
		return adj.data();
	}
	public hashTableSeparateChaining<K, Vertex> vertex()
	{
		return adj;
	}
	//numeros de arcos
	public int E()
	{
		return arcos;
	}

	public void addVertex(K idVertex, V infoVertex)
	{
		Vertex nuevo = new Vertex(idVertex, infoVertex);
		adj.put(idVertex, nuevo);

	}

	public void addEdge(K ideVertexIni, K ideVertexFin, A infoArc)
	{
		Edge edge = new Edge(ideVertexIni, ideVertexFin, infoArc);
		Edge temp = findEdge(edge);
		//Si el arco existe, se actualiza su informacion
		if (temp!=null)
		{
			temp.infoArco = infoArc;
		}
		else
		{
			adj.get(ideVertexIni).arcos().add(edge);
			arcos++;
			listaArcos.add(edge);
		}

	}
	public int test()
	{
		return listaArcos.size();
	}
	public int test2()
	{
		return adj.size(); 
	}


	public Vertex findVertex(K pIdVertex)
	{
		return adj!=null? adj.get(pIdVertex) : null;
	}

	public Edge findEdge(K v1, K v2)
	{
		Edge temp = new Edge(v1,v2,null);
		return findEdge(temp);
	}
	
	public Edge findEdge(Edge pEdge)
	{
		for( int i = 0; i<listaArcos.size();i++)
		{
			if (listaArcos.get(i).compareTo(pEdge)==0)
			{
				return listaArcos.get(i);
			}
		}
		return null;
	}

	public Edge findEdgeForId(K v1, K v2)
	{
		for( int i = 0; i<listaArcos.size();i++)
		{
			if (listaArcos.get(i).getFuente().id().equals(v1)&& listaArcos.get(i).getDestino().id().equals(v2) )
			{
				return listaArcos.get(i);
			}
		}
		return null;
	}

	public void setInfoVertex(K idVertex, V infoVertex)
	{
		adj.get(idVertex).setInfoVer(infoVertex);
	}

	public void setInfoArc(K idVertexIni, K idVertexFin, A infoArc)
	{
		Edge temp = findEdgeForId(idVertexIni, idVertexFin);
		//Si el arco existe, se actualiza su informacion
		if (temp!=null)
		{
			temp.setInfoArc(infoArc);
		}
	}

	public V getInfoVertex(K idVertex)
	{
		return ((Vertex)vertex().get(idVertex)).info();
	}
	
	public A getInfoArc(K idVertexIni, K idVertexFin)
	{
		return findEdge(idVertexIni, idVertexFin).getInfo();
	}
	
	public Iterable <K> adj(K idVertex)
	{
		return (Iterable<K>) findVertex(idVertex).arcos();
	}
	
	public void persistirGrafo()
	{
		JSONObject obj = new JSONObject();
		JSONArray listaVertices = new JSONArray();

		Lista<K> llaves = (Lista<K>) adj.keys();

		for (int i = llaves.size()-1 ; i >=0; i--) {
			Vertex x = adj.get((K) ((Keys)llaves.get(i)).getKey());
			JSONObject verticeObj = new JSONObject();
			JSONObject verticeInnerObj = new JSONObject();
			JSONArray arrArcos = new JSONArray();

			Lista<Edge> arcos = x.arcos();

			for (int k = x.arcos().size()-1; k >= 0 ; k--) {

				JSONObject arcoObj = new JSONObject();
				JSONObject arcoInnerObj = new JSONObject();
				Edge y = arcos.get(k);

				arcoInnerObj.put("fuente", String.valueOf(y.getFuente().id()));
				arcoInnerObj.put("destino", String.valueOf(y.getDestino().id()));

				InfoServicios info = (InfoServicios) y.getInfo();
				JSONObject informacionObj = new JSONObject();
				
				informacionObj.put("trip_total", String.valueOf(info.getTrip_total()));
				informacionObj.put("trip_miles", String.valueOf(info.getTrip_miles()));
				informacionObj.put("trip_seconds", String.valueOf(info.getTrip_seconds()));

				arcoInnerObj.put("informacionArco", informacionObj);

				arcoObj.put("arco" + (x.arcos().size()-k), arcoInnerObj );
				arrArcos.add(arcoObj);
			}

			verticeInnerObj.put("Arcos", arrArcos);
			verticeInnerObj.put("id", x.id());
			
			JSONObject servicioObj = new JSONObject();
			Lista<Servicio> listaServicio = (Lista<Servicio>) x.info();
			for (int j = 0; j < listaServicio.size(); j++) {
				servicioObj.put("tripId", String.valueOf(listaServicio.get(j).getTripId()));
				servicioObj.put("taxiId", String.valueOf(listaServicio.get(j).getTaxiId()));
				servicioObj.put("fechaInicio", String.valueOf(listaServicio.get(j).getFechaInicial()));
				servicioObj.put("fechaFinal", String.valueOf(listaServicio.get(j).getFechaFinal()));
				servicioObj.put("horaInicial", String.valueOf(listaServicio.get(j).getHoraInicial()));
				servicioObj.put("horaFinal", String.valueOf(listaServicio.get(j).getHoraFinal()));
				servicioObj.put("pickUpLatitud", String.valueOf(listaServicio.get(j).getPickupLatitud()));
				servicioObj.put("pickUpLongitud", String.valueOf(listaServicio.get(j).getPickupLongitud()));
				servicioObj.put("tripSeconds", String.valueOf(listaServicio.get(j).getTripSeconds()));
				servicioObj.put("pickUpZone", String.valueOf(listaServicio.get(j).getPickupZone()));
				servicioObj.put("dropOffZone", String.valueOf(listaServicio.get(j).getDropOffZone()));
				servicioObj.put("tripMiles", String.valueOf(listaServicio.get(j).getTripMiles()));
				servicioObj.put("tripTotal", String.valueOf(listaServicio.get(j).getTripTotal()));
				
			}
			verticeInnerObj.put("informacionVertice", servicioObj);

			verticeObj.put("vertice" + (llaves.size()-i), verticeInnerObj);
			listaVertices.add(verticeObj);
		}

		obj.put("Vertices", listaVertices);

		try {

			FileWriter file = new FileWriter(".\\data\\graph.json");
			file.write(obj.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			System.out.println("Error al crear el json");
		}

	}

}