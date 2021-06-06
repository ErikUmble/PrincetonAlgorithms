/* *****************************************************************************
 *  Name: Erik Umble
 *  Date: June 2021
 *  Description: Determine whether or not a baseball team has a chance of winning
 **************************************************************************** */


import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {
    private final int numTeams;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;
    private HashMap<String, Integer> teamNames;
    private FlowNetwork G; // does this need to be global?
    private FordFulkerson ff;
    private boolean trivial; // is this best methodology

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);

        numTeams = in.readInt();
        w = new int[numTeams];
        l = new int[numTeams];
        r = new int[numTeams];
        g = new int[numTeams][numTeams];
        teamNames = new HashMap<String, Integer>();

        for (int i = 0; i < numTeams; i++) {
            teamNames.put(in.readString(), i);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < numTeams; j++) {
                g[i][j] = in.readInt();
            }
        }
    }
    private boolean constructFlowNetwork(int i) {
        /* constructs G with all teams besides i, returns true if a trivial elimination is detected
        Vertex index convention: [0, numTeams-1] represent the team with same number
        except for vertex i, which instead represents the source s
        vertex with index numTeams is the target t
        none of the other vertex indices matter
         */
        int n = numTeams - 1;
        G = new FlowNetwork(n * (n - 1)/2 + n + 2);

        // add edges from each team vertex to the target (besides team i)
        int imaxWins = w[i] + r[i];
        for (int team = 0; team < numTeams; team++) {
            if (team == i) continue;
            if (imaxWins - w[team] < 0) return true;
            FlowEdge e = new FlowEdge(team, numTeams, imaxWins - w[team]);
            G.addEdge(e);
        }
        // for each possible match-up, edge from s to the vertex and edges from the vertex to each of its participating teams
        int count = numTeams + 1;
        for (int team1 = 0; team1 < numTeams; team1++) {
            for (int team2 = team1 + 1; team2 < numTeams; team2++) {
                if ((team1 == i) || (team2 == i)) continue;
                FlowEdge e = new FlowEdge(i, count, g[team1][team2]); // connect s to the game
                G.addEdge(e);
                // connect game to teams involved
                e = new FlowEdge(count, team1, Double.POSITIVE_INFINITY);
                G.addEdge(e);
                e = new FlowEdge(count, team2, Double.POSITIVE_INFINITY);
                G.addEdge(e);
                count++;
            }
        }
        return false;
    }
    // number of teams
    public int numberOfTeams() { return numTeams; }
    // all teams
    public Iterable<String> teams() { return teamNames.keySet(); }
    // number of wins for given team
    public int wins(String team) {
        if (!teamNames.containsKey(team)) throw new IllegalArgumentException();
        return w[teamNames.get(team)];
    }
    // number of losses for given team
    public int losses(String team) {
        if (!teamNames.containsKey(team)) throw new IllegalArgumentException();
        return l[teamNames.get(team)];
    }
    // number of remaining games for given team
    public int remaining(String team) {
        if (!teamNames.containsKey(team)) throw new IllegalArgumentException();
        return r[teamNames.get(team)];
    }
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teamNames.containsKey(team1) || !teamNames.containsKey(team2)) throw new IllegalArgumentException();
        return g[teamNames.get(team1)][teamNames.get(team2)];
    }
    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teamNames.containsKey(team)) throw new IllegalArgumentException();
        int i = teamNames.get(team); // vertex i of G represents s
        trivial = constructFlowNetwork(i);
        if (trivial) return true; // trivial elimination?
        ff = new FordFulkerson(G, i, numTeams);
        // compare the flow value of ff with the max possible outflow from s
        int maxFlow = 0;
        for (FlowEdge e: G.adj(i)) {
            maxFlow += e.capacity();
        }
        if (maxFlow > ff.value()) return true;
        return false;
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!isEliminated(team)) return null;
        Bag<String> bag = new Bag<String>();
        if (trivial) {
            // find team with most wins
            String best = null;
            for (String t: teamNames.keySet()) {
                if (best == null) {
                    best = t;
                    continue;
                }
                if (t.equals(team)) continue;
                if (w[teamNames.get(t)] > w[teamNames.get(best)]) best = t;
            }
            bag.add(best);
        }
        else {
            for (String t: teamNames.keySet()) {
                if (t.equals(team)) continue;
                if (ff.inCut(teamNames.get(t))) bag.add(t);
            }
        }
        return bag;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
