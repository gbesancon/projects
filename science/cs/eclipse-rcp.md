# Eclipse RCP
http://udig.refractions.net/
e(fx)clipse
http://code.google.com/p/worldwindrcp/
Eclipse RCP : IMemento sauvegarde du contexte d'une vue
Eclipse RAP
Knime
Uml : atl + papyrus
http://eclipsemde.blogspot.co.uk/
http://www.eclipse.org/nattable/index.php
http://eclipse.dzone.com/articles/top-10-mistakes-eclipse-plugin
http://www.infoq.com/presentations/Language-Design

Adding Jar
If you have to pick up the 3rd party jars that are already installed in 
the system then you either have to fool with your RCP classpath:

http://help.eclipse.org/galileo/index.jsp?topic=/org.eclipse .platform.doc.isv/reference/misc/runtime-options.html

Or you can set a property -DLIB_LOCATION=/absolute/path/to/lib_dir and 
use external: in your MANIFEST.MF. The you either have to 1) add the 
jars to your Bundle-ClassPath or 2) create their own bundles with their 
Bundle-ClassPath

Bundle-Classpath: .,external:$LIB_LOCATION$/useful.jar

http://www.ant4eclipse.org/

Eclipse
https://yoxos.eclipsesource.com/

EMF
http://wiki.eclipse.org/EMF/Recipes#Recipe:_Generating_Pure_API_With_No_Visible_EMF_Dependencies

XText
http://pettergraff.blogspot.fr/2009/11/xtext-valueconverter.html

P2
http://wiki.eclipse.org/Platform-releng/Platform_Build
http://wiki.eclipse.org/Platform-releng/Platform_Build#Prerequisites
http://eclipse.org/equinox/p2/
http://wiki.eclipse.org/Equinox/p2/Ant_Tasks
http://wiki.eclipse.org/Equinox_p2_Getting_Started#p2_user_interface

Capella
https://www.polarsys.org/projects/polarsys.capella
http://www.polarsys.org/capella/