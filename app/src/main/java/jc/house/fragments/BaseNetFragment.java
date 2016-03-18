package jc.house.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import jc.house.JCListView.XListView;
import jc.house.R;
import jc.house.activities.HomeActivity;
import jc.house.adapters.ListAdapter;
import jc.house.async.FetchLocal;
import jc.house.async.FetchServer;
import jc.house.async.ModelsTask;
import jc.house.global.FetchType;
import jc.house.interfaces.IRefresh;
import jc.house.models.BaseModel;
import jc.house.models.ServerArrayResult;
import jc.house.models.ServerResult;
import jc.house.utils.LogUtils;
import jc.house.utils.SP;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseNetFragment extends BaseFragment implements IRefresh, XListView.IXListViewListener {
    protected XListView xlistView;
    protected boolean isOver;
    protected List<BaseModel> dataSet;
    protected ListAdapter adapter;
    protected int pageSize;
    protected String url;
    protected String tag;
    protected static final String PARAM_PAGE_SIZE = "pageSize";
    protected static final String PARAM_ID = "id";
    protected boolean hasLocalRes;

    protected BaseNetFragment() {
        super();
        this.isOver = false;
        this.dataSet = new ArrayList<>();
    }

    @Override
    public void refresh() {
        if (null != this.xlistView) {
            this.xlistView.smoothScrollToPosition(0);
        }
    }

    protected void resetXListView() {
        this.xlistView.stopLoadMore();
        this.xlistView.stopRefresh();
    }

    protected void handleCode(int code, String tag) {
        switch (code) {
            case ServerResult.CODE_FAILURE:
                LogUtils.debug(tag, "网络请求参数有错误！");
                break;
            case ServerResult.CODE_NO_DATA:
                toastNoMoreData();
                LogUtils.debug(tag, "网络请求连接正常，数据为空！");
                break;
            default:
                break;
        }
        hideDialog();
        resetXListView();
    }

    protected void toastNoMoreData() {
        ToastS("暂时没有更多信息");
    }

    protected void handleFailure() {
        if (!HomeActivity.isNetAvailable) {
            ToastS("当前网络不可用！");
        } else {
            ToastS("服务器连接错误，请重新尝试！");
        }
        hideDialog();
        resetXListView();
    }

    @Override
    public void onRefresh() {
        if (!PRODUCING) {
            this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
        } else {
            this.xlistView.stopRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (!PRODUCING) {
            this.fetchDataFromServer(FetchType.FETCH_TYPE_LOAD_MORE);
        } else {
            this.xlistView.stopLoadMore();
        }
    }

    protected void initListView() {
        this.xlistView = (XListView) this.view.findViewById(R.id.list);
        this.xlistView.setAdapter(this.adapter);
        this.xlistView.setXListViewListener(this);
        this.xlistView.setPullLoadEnable(false);
        this.xlistView.setPullRefreshEnable(false);
    }

    protected Map<String, String> getParams(final FetchType fetchType) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PAGE_SIZE, String.valueOf(pageSize));
        if (FetchType.FETCH_TYPE_LOAD_MORE == fetchType) {
            if (dataSet.size() > 0) {
                params.put(PARAM_ID, String.valueOf((dataSet.get(dataSet.size() - 1)).id));
            }
        }
        return params;
    }

    protected boolean isOver(final FetchType fetchType) {
        if (FetchType.FETCH_TYPE_LOAD_MORE == fetchType && this.isOver) {
            resetXListView();
            toastNoMoreData();
            return true;
        }
        return false;
    }

//    protected void fetchDataFromServer(final FetchType fetchType, final RequestType requestType) {
//        if (this.isOver(fetchType)) {
//            return;
//        }
//        if (requestType == RequestType.POST) {
//            this.client.post(url, new RequestParams(this.getParams(fetchType)), new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    super.onSuccess(statusCode, headers, response);
//                    handleResponse(statusCode, response, fetchType, ServerResult.Type.Array);
//                    LogUtils.debug(tag, "statusCode is " + statusCode + response.toString());
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                    super.onFailure(statusCode, headers, responseString, throwable);
//                    resetXListView();
//                    handleFailure();
//                }
//            });
//        } else {
//            this.client.get(url, new RequestParams(this.getParams(fetchType)), new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    super.onSuccess(statusCode, headers, response);
//                    handleResponse(statusCode, response, fetchType, ServerResult.Type.Array);
//                    LogUtils.debug(tag, "statusCode is " + statusCode + response.toString());
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                    super.onFailure(statusCode, headers, responseString, throwable);
//                    resetXListView();
//                    handleFailure();
//                }
//            });
//        }
//
//    }

    /*
    protected void handleResponse(int statusCode, JSONObject response, final FetchType fetchtype, ServerResult.Type resultType) {
        if (!ServerUtils.isConnectServerSuccess(statusCode, response)) {
            handleFailure();
        } else {
            ServerResult result = ServerUtils.parseServerResponse(response, resultType);
            if (result.isSuccess) {
                handleResponse(result, fetchtype);
            } else {
                handleCode(result.code, "statusCode");
            }
        }
    }

    protected void handleResponse(final ServerResult result, final FetchType fetchType) {
        if (result.isArrayType()) {
            MThreadPool.getInstance().submitParseDataTask(new ParseTask(result, getModelClass()) {
                @Override
                public void onSuccess(List<? extends BaseModel> models) {
                    updateListView((List<BaseModel>) models, fetchType);
                    if (fetchType == FetchType.FETCH_TYPE_REFRESH) {
                        saveToLocal(result.array.toString());
                    }
                }
            });
        } else {
            //暂时没有用到
            MThreadPool.getInstance().submitParseDataTask(new ParseTask(result, getModelClass()) {
                @Override
                public void onSuccess(BaseModel model) {
                    super.onSuccess(model);
                }
            });
        }
    }
    */

    @Override
    protected void setHeader() {
        final PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setPadding(0, 20, 0, 20);
        header.initWithString("JIN CHEN");
        header.setTextColor(Color.RED);
        ptrFrameLayout.setDurationToCloseHeader(1500);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                onRefresh();
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1500);
            }
        });
    }

    protected void updateListView(List<BaseModel> dataSet, final FetchType fetchType) {
        resetXListView();
        hideDialog();
        if (null == dataSet) return;
        int num = dataSet.size();
        if (0 == num) {
            isOver = true;
            toastNoMoreData();
        } else {
            if (fetchType == FetchType.FETCH_TYPE_REFRESH) {
                this.dataSet.clear();
                this.xlistView.setPullLoadEnable(true);
            }
            this.isOver = (num < pageSize);
            this.dataSet.addAll(dataSet);
            this.adapter.notifyDataSetChanged();
            if (fetchType == FetchType.FETCH_TYPE_REFRESH) {
                this.xlistView.smoothScrollToPosition(0);
            }
        }
    }

    protected Class<? extends BaseModel> getModelClass() {
        return BaseModel.class;
    }

    protected void fetchDataFromServer(final FetchType fetchType) {
        if (isOver(fetchType)) {
            return;
        }
        FetchServer.share().postModelsFromServer(this.url, getParams(fetchType), getModelClass(), new ModelsTask() {
            @Override
            public void onSuccess(List<? extends BaseModel> models, ServerArrayResult result) {
                updateListView((List<BaseModel>) models, fetchType);
                saveToLocal(result.array.toString());
            }

            @Override
            public void onFail(String msg) {
                super.onFail(msg);
                handleFailure();
            }

            @Override
            public void onCode(int code) {
                handleCode(code, getActivity().toString());
            }

        });
    }

    protected void loadLocalData() {
//        String content = SP.with(this.getActivity()).getJsonString(this.getModelClass());
//        if (!StringUtils.strEmpty(content)) {
//            ServerResult result = new ServerResult();
//            try {
//                result.array = new JSONArray(content);
//                result.resultType = ServerResult.Type.Array;
//                MThreadPool.getInstance().submitParseDataTask(new ParseTask(result, getModelClass()) {
//                    @Override
//                    public void onSuccess(List<? extends BaseModel> models) {
//                        updateListView((List<BaseModel>) models, FetchType.FETCH_TYPE_REFRESH);
//                        hasLocalRes = true;
//                    }
//                });
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            LogUtils.debug(tag, "Load data from local + " + content + this.getModelClass().toString());
//        }

        FetchLocal.share(getActivity()).fetchModelsFromLocal(getModelClass(), new ModelsTask() {
            @Override
            public void onSuccess(List<? extends BaseModel> models, ServerArrayResult result) {
                updateListView((List<BaseModel>) models, FetchType.FETCH_TYPE_REFRESH);
                hasLocalRes = true;
            }

            @Override
            public void onCode(int code) {
            }
        });
    }

    protected void saveToLocal(String content) {
        SP.with(this.getActivity()).saveJsonString(content, this.getModelClass());
    }

    protected void saveToLocal(String content, Class<? extends BaseModel> cls) {
        SP.with(this.getActivity()).saveJsonString(content, cls);
    }
}
