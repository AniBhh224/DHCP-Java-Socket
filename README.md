# Projet Java — Simulation de serveur DHCP

Ce projet simule un **serveur DHCP** minimaliste en Java, capable de gérer dynamiquement l'attribution d'adresses IP à des clients connectés via TCP. Chaque processus peut agir à la fois comme client **et** serveur.

---

## 📌 Objectifs

- Implémenter les messages fondamentaux du protocole DHCP :
  - `DISCOVER` (demande d'IP)
  - `OFFER` (proposition du serveur)
  - `REQUEST` (confirmation du client)
  - `ACK` (attribution officielle)
- Gérer un **pool d’adresses IP**
- Mettre en place un **système de baux temporaires**
- Permettre la **libération automatique des adresses expirées**
- Échanger des messages une fois l’IP attribuée
- **Logger toutes les opérations** dans un fichier commun `dhcp.log`

---

## 🗂️ Structure des fichiers

| Fichier              | Description |
|----------------------|-------------|
| `AdresseIP.java`     | Représente une adresse IP (état, expiration) |
| `PoolIP.java`        | Gère la liste d'adresses IP disponibles et attribuées |
| `IpUtil.java`        | Conversion IP ↔️ int |
| `Serveur.java`       | Gère les connexions entrantes, baux, réponses DHCP |
| `Client.java`        | Console interactive, communication TCP avec serveur distant |
| `ClientServeur.java` | Lance un serveur local et un client distant |
| `dhcp.log`           | Fichier généré contenant tous les logs horodatés |

---

## 🚀 Compilation & Exécution

### 1. Compilation

```bash
javac *.java
```

### 2. Exécution

Chaque nœud est à la fois client ET serveur.

#### Terminal 1 (exemple) :

```bash
java ClientServeur localhost 9999 9876
```

#### Terminal 2 (exemple) :

```bash
java ClientServeur localhost 9876 9999
```


---

## ⏱️ Comportement au lancement

Lors du lancement du programme via `ClientServeur`, une connexion TCP est automatiquement établie avec le serveur distant **après 5 secondes**.

Cependant, **aucune IP ne vous sera attribuée tant que vous n'avez pas tapé la commande `connect`** dans la console. Cette commande déclenche l'envoi d'un message `DISCOVER` pour démarrer la procédure d'attribution.


---

## 🧪 Commandes disponibles dans le client

En ligne de commande (une fois connecté au programme) :

- `connect` : envoie un DISCOVER au serveur distant pour obtenir une IP
- `show` : affiche l’IP attribuée + l’état du pool local
- `exit` : quitte le programme
- `[message libre]` : envoie un message au serveur distant (si IP attribuée)



## 🧠 Auteur

Projet réalisé dans le cadre d'un TP réseau Java par **Anis Bougherbal** et **Otman Louahdi**.
