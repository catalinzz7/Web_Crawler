
public interface iCrawl {
    boolean check_robots();

    void download_page();

    void find_URL();

    void remove_disallow_url();

    void recursive_download();

    void remove_existing_page();
}
