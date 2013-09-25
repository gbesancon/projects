// Read inputs from System.in, Write outputs to System.out.
// Your class name has to be Solution

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

class Solution1
{
  private static Map<Integer, List<Integer>> buildRelationships()
  {
    Map<Integer, List<Integer>> relationships = new HashMap<Integer, List<Integer>>();

    Scanner in = new Scanner(System.in);
    try
    {
      int nbRelationships = in.nextInt();

      for (int i = 0; i < nbRelationships; i++)
      {
        int a = in.nextInt();
        int b = in.nextInt();

        List<Integer> relationshipsFor = relationships.get(a);
        if (relationshipsFor == null)
        {
          relationshipsFor = new ArrayList<Integer>();
          relationships.put(a, relationshipsFor);
        }

        if (!relationshipsFor.contains(b))
        {
          relationshipsFor.add(b);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      in.close();
    }
    return relationships;
  }

  private static List<List<Integer>> performInfluenceChains(Map<Integer, List<Integer>> relationships)
  {
    List<List<Integer>> influenceChains = new ArrayList<List<Integer>>();

    for (Entry<Integer, List<Integer>> entries : relationships.entrySet())
    {
      Integer relater = entries.getKey();
      for (Integer related : entries.getValue())
      {
        List<Integer> precedingInfluenceChain = new ArrayList<Integer>();
        precedingInfluenceChain.add(relater);
        precedingInfluenceChain.add(related);

        influenceChains.addAll(performInfluenceChains(relationships, precedingInfluenceChain));
      }
    }

    return influenceChains;
  }

  private static List<List<Integer>> performInfluenceChains(Map<Integer, List<Integer>> relationships,
      List<Integer> precedingInfluenceChain)
  {
    List<List<Integer>> influenceChains = new ArrayList<List<Integer>>();

    List<Integer> relateds = relationships.get(precedingInfluenceChain.get(precedingInfluenceChain.size() - 1));
    if (relateds != null)
    {
      for (Integer related : relateds)
      {
        if (!precedingInfluenceChain.contains(related))
        {
          List<Integer> newPrecedingInfluenceChain = new ArrayList<Integer>();
          newPrecedingInfluenceChain.addAll(precedingInfluenceChain);
          newPrecedingInfluenceChain.add(related);
          influenceChains.addAll(performInfluenceChains(relationships, newPrecedingInfluenceChain));
        }
        else
        {
          influenceChains.add(precedingInfluenceChain);
        }
      }
    }
    else
    {
      influenceChains.add(precedingInfluenceChain);
    }

    return influenceChains;
  }

  private static List<Integer> performSizeOfInfluenceChain(List<List<Integer>> influenceChains)
  {
    List<Integer> influenceChainsSize = new ArrayList<Integer>();
    for (int i = 0; i < influenceChains.size(); i++)
    {
      influenceChainsSize.add(influenceChains.get(i).size());
    }
    return influenceChainsSize;
  }

  public static void main(String args[])
  {
    Map<Integer, List<Integer>> relationships = buildRelationships();
    List<List<Integer>> influenceChains = performInfluenceChains(relationships);
    List<Integer> influenceChainsSize = performSizeOfInfluenceChain(influenceChains);
    Integer max = Collections.max(influenceChainsSize);
    System.out.println(max);
  }
}
