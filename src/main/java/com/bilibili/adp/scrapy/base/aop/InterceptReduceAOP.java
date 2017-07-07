package com.bilibili.adp.scrapy.base.aop;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bilibili.adp.scrapy.base.util.RedisCacheUtil;

@Aspect
@Component
@Order(1)
public class InterceptReduceAOP {
	
	private Log logger = LogFactory.getLog(InterceptReduceAOP.class);
	@Resource
	private RedisCacheUtil redisCacheUtil;
	@Pointcut(value = "@annotation(reduce)")
	private void getPointcut(MyReduce reduce){
		
	}
	@Around("getPointcut(reduce)")
	public Object preProcessQueryPattern(ProceedingJoinPoint point,MyReduce reduce) throws Throwable{
        String targetName = point.getTarget().getClass().getName();
        String simpleName = point.getTarget().getClass().getSimpleName();
        String methodName = point.getSignature().getName();
        Object[] arguments = point.getArgs();
        
        String key = null;
////        没传key
//        if (intercept.curDate().length() > 0) {
//        	key = "'"+simpleName+"."+methodName + ".'+" +  intercept.curDate();
//        }else{
            key = simpleName+"."+methodName;
          
//        }
         
        String[] paramNames = ParamNameMap.get(key);
        if (paramNames == null){
//          反射得到形参名称
            paramNames = ReflectParamNames.getNames(targetName, methodName,arguments);
            ParamNameMap.put(key, paramNames);
        }
        
         
//        if(reduce.curDate().length() > 0){
////          spring EL 表达式
//        	int startFix = reduce.curDate().indexOf("#");
//        	if(startFix >= 0){
//        		key = SpelParser.getKey(key, reduce.curDate(), paramNames, arguments);
//        	}
//        }
         
        Object object = redisCacheUtil.getCacheValue(reduce.cacheName(), key);
        if(object != null){
        	int v = Integer.valueOf(object.toString());
        	if(v == 0){
        		return null;
        	}
        }
//      重新加载时不走缓存
//        if(!Cache.reLoad()){
            
//        }
         
         
//        if (object!=null){
//            return object;
//        }
         Object target = point.proceed();
         if (target != null){
//        	redisCacheUtil.putCacheValue(RedisCacheConstant.db_etl_not_clean_table, key, 1);
//           memcache.set(key, target, Cache.second());
//           logger.info("+++ key:"+key+" set cache 耗时： " + (endTime - startTime)  + "毫秒");
         }
            //拦截的放参数类型
//       Class[] parameterTypes = ((MethodSignature)point.getSignature()).getMethod().getParameterTypes();
        return target;
	}
}
