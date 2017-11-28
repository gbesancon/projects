package org.benhur.utility.emf.resource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class EMFResourceUtility
{
  public static URI getURI(String filePath)
  {
    return getURI(new File(filePath));
  }

  public static URI getURI(File file)
  {
    return URI.createFileURI(file.getPath());
  }

  public static String getWorkspaceFilepath(URI uri)
  {
    String filePath = null;
    if (uri != null)
    {
      if (uri.isPlatformResource())
      {
        filePath = (uri.toString().substring(18));
      }
    }
    return filePath;
  }

  public static IFile getWorkspaceIFile(URI uri)
  {
    IFile result = null;
    if (uri != null)
    {
      String filePath = getWorkspaceFilepath(uri.trimFragment());
      IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
      if (filePath == null)
      {
        String localPath = uri.toFileString();
        if (localPath == null)
        {
          IPath path = new Path(uri.path());
          IResource res = workspaceRoot.findMember(path);
          if (res != null && res.getType() == IResource.FILE)
          {
            return (IFile) res;
          }
          return null;
        }
        else
        {
          IPath location = Path.fromOSString(localPath);
          @SuppressWarnings("deprecation")
          IFile[] files = workspaceRoot.findFilesForLocation(location);
          if (files == null || files.length == 0)
            return null;
          return files[0];
        }
      }
      else
      {
        IResource workspaceResource = workspaceRoot.findMember(filePath);
        if ((workspaceResource == null) || (workspaceResource.getType() != IResource.FILE))
        {
          return null;
        }
        return (IFile) workspaceResource;
      }
    }
    return result;
  }

  public static String getResourceFilepath(Resource resource)
  {
    String filePath = "";
    if (resource != null)
    {
      URI uri = resource.getURI();
      IFile file = getWorkspaceIFile(uri);
      if (file == null)
      {
        filePath = uri != null ? uri.toFileString() : "";
      }
      else
      {
        filePath = file.getFullPath().toString();
      }
    }

    if (filePath == null)
    {
      filePath = "";
    }

    return filePath;
  }

  public static Map<Object, Object> getDefaultLoadSaveOptions()
  {
    Map<Object, Object> options = new HashMap<Object, Object>();
    options.put(XMLResource.OPTION_ENCODING, "UTF-8");
    return options;
  }

  public static void saveObject(EObject object, Map<Object, Object> saveOptions)
  {
    try
    {
      if (object != null && object.eResource() != null)
      {
        object.eResource().save(saveOptions);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public static void saveResource(EObject object, String path, Map<String, Resource.Factory> factories,
      Map<Object, Object> saveOptions)
  {
    try
    {
      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Registry.DEFAULT_EXTENSION,
                                                                        new XMIResourceFactoryImpl());
      if (factories != null)
      {
        for (Entry<String, Resource.Factory> entry : factories.entrySet())
        {
          Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(entry.getKey(), entry.getValue());
        }
      }

      ResourceSet resourceSet = new ResourceSetImpl();
      Resource resource = resourceSet.createResource(URI.createFileURI(path));
      resource.getContents().add(object);
      resource.save(saveOptions);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public static Resource loadResource(URI uri, Map<String, EPackage> packages, Map<String, Resource.Factory> factories,
      Map<Object, Object> loadOptions)
  {
    Resource resource = null;
    ResourceSet resourceSet = new ResourceSetImpl();
    try
    {
      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Registry.DEFAULT_EXTENSION,
                                                                        new XMIResourceFactoryImpl());
      if (factories != null)
      {
        for (Entry<String, Resource.Factory> entry : factories.entrySet())
        {
          Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(entry.getKey(), entry.getValue());
        }
      }

      if (packages != null)
      {
        for (Entry<String, EPackage> entry : packages.entrySet())
        {
          resourceSet.getPackageRegistry().put(entry.getKey(), entry.getValue());
        }
      }
      resource = resourceSet.getResource(uri, true);
      resource.load(loadOptions);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return resource;
  }
}
