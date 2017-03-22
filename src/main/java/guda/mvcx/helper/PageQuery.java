package guda.mvcx.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by well on 2017/3/22.
 */
public class PageQuery {

    private int maxShowPage = 5;

    private int pageSize = 10;

    private int startRow = 0;

    private int pageNo = 1;

    private int totalCount;


    private List<Integer> pages = new ArrayList<Integer>();

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize <= 1) {
            pageSize = 10;
        }
        startRow = (pageNo - 1) * pageSize;
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo <= 1) {
            pageNo = 1;
        }
        startRow = (pageNo - 1) * pageSize;
        this.pageNo = pageNo;
    }


    public int getMaxPage() {
        if (totalCount <= 1) {
            return 1;
        }
        if (pageSize <= 1) {
            return totalCount;
        }
        return (totalCount + pageSize - 1) / pageSize;
    }


    public int getPageCount() {
        return getMaxPage();
    }

    public boolean hasNextPage() {
        int currentPage = getCurrentPage();
        return currentPage < getMaxPage();
    }

    public boolean hasPrevPage() {
        int currentPage = getCurrentPage();
        return currentPage > 1;
    }

    public int getNextPage() {
        int currentPage = getCurrentPage();
        if (hasNextPage()) {
            return (currentPage + 1);
        }
        return currentPage;
    }

    public int getPrevPage() {
        int currentPage = getCurrentPage();
        if (hasPrevPage()) {
            return currentPage - 1;
        }
        return currentPage;
    }

    public boolean turnNext() {
        if (hasNextPage()) {
            pageNo = getNextPage();
            return true;
        }
        return false;
    }

    public boolean turnPrev() {
        if (hasPrevPage()) {
            pageNo = getPrevPage();
            return true;
        }
        return false;
    }

    public boolean turn(int page) {
        int maxPage = getMaxPage();
        if (page > maxPage) {
            pageNo = maxPage;
            return false;
        }
        if (page < 1) {
            pageNo = 1;
            return false;
        }
        pageNo = page;
        return true;
    }

    public int getCurrentPage() {
        int maxPage = getMaxPage();
        if (pageNo > maxPage) {
            return maxPage;
        }
        if (maxPage < 1) {
            return 1;
        }
        return pageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        int page = (totalCount + pageSize - 1) / pageSize + 1;
        if (pageNo > page) {
            pageNo = page;
        }
        countPages();
    }

    private void countPages() {
        int page = getMaxPage();
        if (page <= maxShowPage) {
            for (int i = 1; i <= page; ++i) {
                pages.add(i);
            }
        } else {
            List<Integer> prefix = new ArrayList<Integer>();
            List<Integer> suffix = new ArrayList<Integer>();
            if (pageNo < 3) {
                for (int i = 1; i < pageNo + 1; ++i) {
                    prefix.add(i);
                }
            } else {
                if (page - pageNo == 0) {
                    for (int i = 4; i > -1; --i) {
                        if (pageNo - i > 0) {
                            prefix.add(pageNo - i);
                        }
                    }
                } else if (page - pageNo == 1) {
                    for (int i = 3; i > -1; --i) {
                        if (pageNo - i > 0) {
                            prefix.add(pageNo - i);
                        }
                    }
                } else {
                    prefix.add(pageNo - 2);
                    prefix.add(pageNo - 1);
                    prefix.add(pageNo);
                }
            }
            int size = maxShowPage - prefix.size();
            for (int i = pageNo + 1; (i <= page && i <= pageNo + size); ++i) {
                suffix.add(i);
            }
            pages.addAll(prefix);
            pages.addAll(suffix);

        }
    }

    public int getMaxShowPage() {
        return maxShowPage;
    }

    public void setMaxShowPage(int maxShowPage) {
        this.maxShowPage = maxShowPage;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageQuery)) return false;

        PageQuery baseQuery = (PageQuery) o;

        if (maxShowPage != baseQuery.maxShowPage) return false;
        if (pageNo != baseQuery.pageNo) return false;
        if (pageSize != baseQuery.pageSize) return false;
        if (startRow != baseQuery.startRow) return false;
        if (totalCount != baseQuery.totalCount) return false;
        if (pages != null ? !pages.equals(baseQuery.pages) : baseQuery.pages != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = maxShowPage;
        result = 31 * result + pageSize;
        result = 31 * result + startRow;
        result = 31 * result + pageNo;
        result = 31 * result + totalCount;
        result = 31 * result + (pages != null ? pages.hashCode() : 0);
        return result;
    }
}
