/**
 * Classname iCrawl
 * Implementeaza interfata clasei Crawl
 *
 * @author Dan-Cristian Gutiu
 */
public interface iCrawl {
    /**
     * Declararea metodelor,listelor de argumente,valorilor
     * intoarse pentru clasa Crawl
     */
    boolean check_robots();
    void download_page();
    void find_URL();
    void remove_disallow_url();
    void recursive_download();
    void remove_existing_page();
}
