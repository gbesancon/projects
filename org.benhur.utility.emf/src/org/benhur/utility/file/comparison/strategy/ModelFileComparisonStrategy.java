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
  @Override
  public boolean compareFile(File file1, File file2)
  {
    boolean result = false;

    try
    {
      // register XMI resource factory for all other extensions
      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION,
                                                                        new XMIResourceFactoryImpl());

      ResourceSet resourceSet = new ResourceSetImpl();

      // Loading resources
      EObject model1 = ModelUtils.load(file1, resourceSet);
      EObject model2 = ModelUtils.load(file2, resourceSet);

      // matching models
      // MatchModel match = MatchService.doMatch(model1, model2, Collections.<String, Object> emptyMap());
      MatchModel match = MatchService.doContentMatch(model1, model2, Collections.<String, Object> emptyMap());

      // matching engine and matching
      GenericMatchEngine eng = new GenericMatchEngine();
      match = eng.contentMatch(model1, model2, Collections.<String, Object> emptyMap());

      // Differencing models
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
