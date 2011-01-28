/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */
package fr.certu.chouette.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import fr.certu.chouette.common.ChouetteException;
import fr.certu.chouette.core.CoreException;
import fr.certu.chouette.core.CoreExceptionCode;
import fr.certu.chouette.dao.IDaoTemplate;
import fr.certu.chouette.filter.DetailLevelEnum;
import fr.certu.chouette.filter.Filter;
import fr.certu.chouette.model.neptune.Company;
import fr.certu.chouette.model.neptune.Line;
import fr.certu.chouette.model.neptune.NeptuneIdentifiedObject;
import fr.certu.chouette.model.neptune.PTNetwork;
import fr.certu.chouette.model.neptune.Route;
import fr.certu.chouette.model.user.User;
import fr.certu.chouette.plugin.exchange.FormatDescription;
import fr.certu.chouette.plugin.exchange.IExportPlugin;
import fr.certu.chouette.plugin.exchange.IImportPlugin;
import fr.certu.chouette.plugin.exchange.ParameterValue;
import fr.certu.chouette.plugin.report.Report;
import fr.certu.chouette.plugin.report.ReportHolder;
import fr.certu.chouette.plugin.report.ReportItem;
import fr.certu.chouette.plugin.validation.IValidationPlugin;
import fr.certu.chouette.plugin.validation.ValidationStepDescription;

/**
 * 
 */
/**
 * @author michel
 *
 * @param <T>
 */
/**
 * @author michel
 *
 * @param <T>
 */
/**
 * @author michel
 *
 * @param <T>
 */
public class DummyManager<T extends NeptuneIdentifiedObject> implements INeptuneManager<T>
{
	public static DummyManager<PTNetwork> createPTNetworkManager()
	{
		return new DummyManager<PTNetwork>();
	}
	public static DummyManager<Line> createLineManager()
	{
		return new DummyManager<Line>();
	}
	public static DummyManager<Company> createCompanyManager()
	{
		return new DummyManager<Company>();
	}
	public static DummyManager<Route> createRouteManager()
	{
		return new DummyManager<Route>();
	}
	
	
	// data storage access
	@Getter @Setter

 private IDaoTemplate<T> dao; 

	private Map<String,IImportPlugin<T>> importPluginMap = new HashMap<String, IImportPlugin<T>>();
	private Map<String,IExportPlugin<T>> exportPluginMap = new HashMap<String, IExportPlugin<T>>();
	private Map<String,IExportPlugin<T>> exportDeletionPluginMap = new HashMap<String, IExportPlugin<T>>();
	private Map<String,IValidationPlugin<T>> validationPluginMap = new HashMap<String, IValidationPlugin<T>>();


	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#addImportPlugin(fr.certu.chouette.plugin.exchange.IImportPlugin)
	 */
	@Override
	public void addImportPlugin(IImportPlugin<T> plugin)
	{
		importPluginMap.put(plugin.getDescription().getName(),plugin);
	}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#addExportPlugin(fr.certu.chouette.plugin.exchange.IExportPlugin)
	 */
	@Override
	public void addExportPlugin(IExportPlugin<T> plugin)
	{
		exportPluginMap.put(plugin.getDescription().getName(),plugin);
	}
	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#addExportDeletionPlugin(fr.certu.chouette.plugin.exchange.IExportPlugin)
	 */
	@Override
	public void addExportDeletionPlugin(IExportPlugin<T> plugin)
	{
		exportDeletionPluginMap.put(plugin.getDescription().getName(),plugin);
	}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#addValidationPlugin(fr.certu.chouette.plugin.validation.IValidationPlugin)
	 */
	@Override
	public void addValidationPlugin(IValidationPlugin<T> plugin)
	{
		validationPluginMap.put(plugin.getDescription().getName(),plugin);
	}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#addNew(fr.certu.chouette.model.user.User, fr.certu.chouette.model.neptune.NeptuneObject)
	 */
	@Override
	public T addNew(User user, T bean) throws ChouetteException 
	{
		if (getDao() == null) throw new CoreException(CoreExceptionCode.NO_DAO_AVAILABLE,"unavailable resource");
		// TODO : check user access
		getDao().save(bean);
		return bean;
	}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#exists(fr.certu.chouette.model.user.User, fr.certu.chouette.model.neptune.NeptuneObject)
	 */
	@Override
	public boolean exists(User user, T bean) throws ChouetteException
	{
		if (getDao() == null) throw new CoreException(CoreExceptionCode.NO_DAO_AVAILABLE,"unavailable resource");
		// TODO : check user access
		if (bean.getId() != null)
		{
			return getDao().exists(bean.getId());
		}
		if (bean.getObjectId() != null)
		{
			return getDao().exists(bean.getObjectId());
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#get(fr.certu.chouette.model.user.User, fr.certu.chouette.filter.Filter, fr.certu.chouette.filter.DetailLevelEnum)
	 */
	@Override
	public T get(User user, Filter filter, DetailLevelEnum level) throws ChouetteException
	{
		if (getDao() == null) throw new CoreException(CoreExceptionCode.NO_DAO_AVAILABLE,"unavailable resource");
		// TODO : check user access
		if (filter.getType().equals(Filter.Type.EQUALS))
		{
			T bean = null;
			if (filter.getAttribute().equals("id"))
			{
				bean = getDao().get((Long) filter.getFirstValue());
			}
			if (filter.getAttribute().equals("objectId"))
			{
				bean = getDao().getByObjectId((String) filter.getFirstValue());
			}
			bean.expand(level);
			return bean;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#getAll(fr.certu.chouette.model.user.User, fr.certu.chouette.filter.Filter, fr.certu.chouette.filter.DetailLevelEnum)
	 */
	@Override
	public List<T> getAll(User user, Filter filter, DetailLevelEnum level) throws ChouetteException
	{
		if (getDao() == null) throw new CoreException(CoreExceptionCode.NO_DAO_AVAILABLE,"unavailable resource");
		// TODO : check user access
		List<T> beans =  getDao().select(filter);
		for (T bean : beans)
		{
			bean.expand(level);
		}

		return beans;
	}

	@Override
	public T getNewInstance(User user) throws ChouetteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(User user, T bean) throws ChouetteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(User user, T bean, DetailLevelEnum level)
			throws ChouetteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRemovable(User user, T bean) throws ChouetteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void remove(User user, T bean) throws ChouetteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int removeAll(User user, Filter filter) throws ChouetteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#getImportFormats(fr.certu.chouette.model.user.User)
	 */
	@Override
	public List<FormatDescription> getImportFormats(User user)
	throws ChouetteException 
	{
		List<FormatDescription> formats = new ArrayList<FormatDescription>();

		for (IImportPlugin<T> plugin : importPluginMap.values()) 
		{
			formats.add(plugin.getDescription());
		}

		return formats;
	}

	@Override
	public List<T> doImport(User user, String formatDescriptor,List<ParameterValue> parameters,ReportHolder report) throws ChouetteException 
	{
		IImportPlugin<T> plugin = importPluginMap.get(formatDescriptor);
		if (plugin == null) throw new CoreException(CoreExceptionCode.NO_PLUGIN_AVAILABLE,"unknown format :"+formatDescriptor);

		return plugin.doImport(parameters,report);


	}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#saveAll(fr.certu.chouette.model.user.User, java.util.List)
	 */
	@Override
	public void saveAll(User user, List<T> beans) throws ChouetteException 
	{
		if (getDao() == null) throw new CoreException(CoreExceptionCode.NO_DAO_AVAILABLE,"unavailable resource");
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.NeptuneBeanManager#saveOrUpdateAll(fr.certu.chouette.model.user.User, java.util.List)
	 */
	@Override
	public void saveOrUpdateAll(User user, List<T> beans) throws ChouetteException
	{
		if (getDao() == null) throw new CoreException(CoreExceptionCode.NO_DAO_AVAILABLE,"unavailable resource");
		// TODO Auto-generated method stub

	}


	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#getExportFormats(fr.certu.chouette.model.user.User)
	 */
	@Override
	public List<FormatDescription> getExportFormats(User user)
	throws ChouetteException 
	{
		List<FormatDescription> formats = new ArrayList<FormatDescription>();

		for (IExportPlugin<T> plugin : exportPluginMap.values()) 
		{
			formats.add(plugin.getDescription());
		}
		return formats;
	}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#doExport(fr.certu.chouette.model.user.User, java.util.List, java.lang.String, java.util.List, fr.certu.chouette.plugin.report.ReportHolder)
	 */
	@Override
	public void doExport(User user, List<T> beans, String formatDescriptor,
			List<ParameterValue> parameters,ReportHolder report) throws ChouetteException 
			{
		IExportPlugin<T> plugin = exportPluginMap.get(formatDescriptor);
		if (plugin == null) throw new CoreException(CoreExceptionCode.NO_PLUGIN_AVAILABLE,"unknown format :"+formatDescriptor);

		plugin.doExport(beans,parameters,report);

			}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#getDeleteExportFormats(fr.certu.chouette.model.user.User)
	 */
	@Override
	public List<FormatDescription> getDeleteExportFormats(User user)
	throws ChouetteException 
	{
		List<FormatDescription> formats = new ArrayList<FormatDescription>();

		for (IExportPlugin<T> plugin : exportDeletionPluginMap.values()) 
		{
			formats.add(plugin.getDescription());
		}
		return formats;
	}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#doExportDeleted(fr.certu.chouette.model.user.User, java.util.List, java.lang.String, java.util.List, fr.certu.chouette.plugin.report.ReportHolder)
	 */
	@Override
	public void doExportDeleted(User user, List<T> beans,
			String formatDescriptor, List<ParameterValue> parameters,ReportHolder report)
	throws ChouetteException 
	{
		IExportPlugin<T> plugin = exportDeletionPluginMap.get(formatDescriptor);
		if (plugin == null) throw new CoreException(CoreExceptionCode.NO_PLUGIN_AVAILABLE,"unknown format :"+formatDescriptor);

		plugin.doExport(beans,parameters,report);

	}



	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#validate(fr.certu.chouette.model.user.User, fr.certu.chouette.model.neptune.NeptuneObject)
	 */
	@Override
	public Report validate(User user, T bean) throws ChouetteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#getValidationSteps(fr.certu.chouette.model.user.User)
	 */
	@Override
	public List<ValidationStepDescription> getValidationSteps(User user)
	throws ChouetteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.certu.chouette.manager.INeptuneManager#validateStep(fr.certu.chouette.model.user.User, fr.certu.chouette.model.neptune.NeptuneObject, java.lang.String)
	 */
	@Override
	public ReportItem validateStep(User user, T bean, String stepDescriptor)
	throws ChouetteException {
		// TODO Auto-generated method stub
		return null;
	}
}
