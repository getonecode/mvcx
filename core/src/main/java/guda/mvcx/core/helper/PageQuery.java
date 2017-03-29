package guda.mvcx.core.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by well on 2017/3/22.
 */
public class PageQuery {

    private int maxShowPage = 5;

    private int pageSize = 10;

    private int startRow = 0;

    private int page = 1;

    private int totalCount;


    private List<Integer> pages = new ArrayList<Integer>();

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize <= 1) {
            pageSize = 10;
        }
        startRow = (page - 1) * pageSize;
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page <= 1) {
            page = 1;
        }
        startRow = (page - 1) * pageSize;
        this.page = page;
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
            page = getNextPage();
            return true;
        }
        return false;
    }

    public boolean turnPrev() {
        if (hasPrevPage()) {
            page = getPrevPage();
            return true;
        }
        return false;
    }

    public boolean turn(int page) {
        int maxPage = getMaxPage();
        if (page > maxPage) {
            this.page = maxPage;
            return false;
        }
        if (page < 1) {
            this.page = 1;
            return false;
        }
        this.page = page;
        return true;
    }

    public int getCurrentPage() {
        int maxPage = getMaxPage();
        if (page > maxPage) {
            return maxPage;
        }
        if (maxPage < 1) {
            return 1;
        }
        return page;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        int page = (totalCount + pageSize - 1) / pageSize + 1;
        if (this.page > page) {
            this.page = page;
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
            if (this.page < 3) {
                for (int i = 1; i < this.page + 1; ++i) {
                    prefix.add(i);
                }
            } else {
                if (page - this.page == 0) {
                    for (int i = 4; i > -1; --i) {
                        if (this.page - i > 0) {
                            prefix.add(this.page - i);
                        }
                    }
                } else if (page - this.page == 1) {
                    for (int i = 3; i > -1; --i) {
                        if (this.page - i > 0) {
                            prefix.add(this.page - i);
                        }
                    }
                } else {
                    prefix.add(this.page - 2);
                    prefix.add(this.page - 1);
                    prefix.add(this.page);
                }
            }
            int size = maxShowPage - prefix.size();
            for (int i = this.page + 1; (i <= page && i <= this.page + size); ++i) {
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
        if (page != baseQuery.page) return false;
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
        result = 31 * result + page;
        result = 31 * result + totalCount;
        result = 31 * result + (pages != null ? pages.hashCode() : 0);
        return result;
    }
}
