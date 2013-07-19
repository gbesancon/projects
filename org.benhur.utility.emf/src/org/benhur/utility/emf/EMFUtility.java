package org.benhur.utility.emf;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class EMFUtility
{
  public static EList<EObject> getAllContentsAndCrossReferences(EObject object)
  {
    EList<EObject> result = new BasicEList<EObject>();
    getAllContentsAndCrossReferences(object, result);
    return result;
  }

  protected static void getAllContentsAndCrossReferences(EObject object, List<EObject> allContentsAndCrossReferences)
  {
    // Contents
    EList<EObject> contents = object.eContents();
    for (EObject content : contents)
    {
      if (!allContentsAndCrossReferences.contains(content))
      {
        allContentsAndCrossReferences.add(content);
        // Recurse in content
        getAllContentsAndCrossReferences(content, allContentsAndCrossReferences);
      }
    }

    // Cross References
    EList<EObject> references = object.eCrossReferences();
    for (EObject reference : references)
    {
      if (!allContentsAndCrossReferences.contains(reference))
      {
        // Resolve if proxy
        if (reference.eIsProxy())
        {
          EcoreUtil.resolveAll(object);
        }
        allContentsAndCrossReferences.add(reference);
        // Recurse in cross reference
        getAllContentsAndCrossReferences(reference, allContentsAndCrossReferences);
      }
    }
  }

  public static List<Resource> getAllResources(Resource resource)
  {
    List<Resource> result = new ArrayList<Resource>();
    for (EObject object : resource.getContents())
    {
      getAllResources(object, result);
    }
    return result;
  }

  public static List<Resource> getAllResources(EObject object)
  {
    List<Resource> result = new ArrayList<Resource>();
    getAllResources(object, result);
    return result;
  }

  protected static void getAllResources(EObject object, List<Resource> allResources)
  {
    for (EObject contentOrReference : getAllContentsAndCrossReferences(object))
    {
      Resource resource = contentOrReference.eResource();
      if (resource != null && !allResources.contains(resource))
      {
        allResources.add(resource);
      }
    }
  }

  public static <T extends EObject> List<T> getAllContents(EObject object, Class<T> aClass)
  {
    List<T> contents = new ArrayList<T>();
    getAllContents(object, aClass, contents);
    return contents;
  }

  @SuppressWarnings("unchecked")
  protected static <T extends EObject> void getAllContents(EObject object, Class<T> aClass, List<T> contents)
  {
    if (object != null)
    {
      if (aClass.isInstance(object))
      {
        contents.add((T) object);
      }
      for (EObject content : object.eContents())
      {
        getAllContents(content, aClass, contents);
      }
    }
  }

  public static <T extends EObject> List<T> getAllContainers(EObject object, Class<T> aClass)
  {
    List<T> containers = new ArrayList<T>();
    getAllContainers(object, aClass, containers);
    return containers;
  }

  @SuppressWarnings("unchecked")
  protected static <T extends EObject> void getAllContainers(EObject object, Class<T> aClass, List<T> containers)
  {
    if (object != null)
    {
      if (aClass.isInstance(object))
      {
        containers.add((T) object);
      }
      if (object.eContainer() != null)
      {
        getAllContainers(object.eContainer(), aClass, containers);
      }
    }
  }

  public static EObject getTopContainer(EObject object)
  {
    EObject topContainer = object;
    if (object != null)
    {
      if (object.eContainer() != null)
      {
        topContainer = getTopContainer(object.eContainer());
      }
    }
    return topContainer;
  }

  public static <T extends EObject> T getTopContainer(EObject object, Class<T> aClass)
  {
    T topContainer = null;
    List<T> containers = getAllContainers(object, aClass);
    if (containers.size() != 0)
    {
      topContainer = containers.get(containers.size() - 1);
    }
    return topContainer;
  }
}
