package org.benhur.utility.file.comparison.strategy;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.engine.GenericMatchEngine;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class ModelFileComparisonStrategy implements IFileComparisonStrategy
{
  public boolean compareFile(File file1, File file2)
  {
    boolean result = false;

    try
    {
      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION,
                                                                        new XMIResourceFactoryImpl());

      ResourceSet resourceSet = new ResourceSetImpl();

      EObject model1 = ModelUtils.load(file1, resourceSet);
      EObject model2 = ModelUtils.load(file2, resourceSet);

      MatchModel match = MatchService.doContentMatch(model1, model2, Collections.<String, Object> emptyMap());

      GenericMatchEngine engine = new GenericMatchEngine();
      match = engine.contentMatch(model1, model2, Collections.<String, Object> emptyMap());

      DiffModel diff = DiffService.doDiff(match);

      if (diff.getOwnedElements().size() == 0)
      {
        result = true;
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    return result;
  }

}
