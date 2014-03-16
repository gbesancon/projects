package org.benhur.utility.emf.validation.custom;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

public interface IEMFModelCustomValidator
{
  <T extends EObject> IStatus validate(T model, IProgressMonitor progressMonitor);
}
