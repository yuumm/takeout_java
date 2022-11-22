// 查询列表接口
const getCategoryPage = (params) => {
  return $axios({
    url: '/category/page',
    method: 'get',
    params
  })
}

// 编辑页面反查详情接口
const queryCategoryById = (id) => {
  return $axios({
    url: `/category/${id}`,
    method: 'get'
  })
}

// 删除当前列的接口。参数一定要和后端controller中的参数一致，否则无法自动匹配
const deleCategory = (id) => {
  return $axios({
    url: '/category',
    method: 'delete',
    params: { id }
  })
}

// 修改接口
const editCategory = (params) => {
  return $axios({
    url: '/category',
    method: 'put',
    data: { ...params }
  })
}

// 新增接口
const addCategory = (params) => {
  return $axios({
    url: '/category',
    method: 'post',
    data: { ...params }
  })
}